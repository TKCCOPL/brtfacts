package com.brtfacts.event;

import com.brtfacts.BrtFacts;
import com.brtfacts.integration.curios.CuriosCompat;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BrtFacts.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (player.getItemBySlot(EquipmentSlot.FEET).getItem() == BrtFacts.BUNNY_SLIPPERS.get()) {
                event.setDistance(0.0F);
                event.setCanceled(true);
                return;
            }
            if (ModList.get().isLoaded("curios")
                    && CuriosCompat.isItemEquippedInCurios(player, BrtFacts.BUNNY_SLIPPERS.get())) {
                event.setDistance(0.0F);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        boolean hasCurios = ModList.get().isLoaded("curios");

        // Magma Heart Belt: set attacker on fire
        if (hasCurios && CuriosCompat.isItemEquippedInCurios(player, BrtFacts.MAGMA_HEART_BELT.get())) {
            Entity attacker = event.getSource().getEntity();
            if (attacker instanceof LivingEntity livingAttacker && attacker != player) {
                livingAttacker.setRemainingFireTicks(80); // 4 seconds of fire
                if (player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.FLAME,
                            livingAttacker.getX(), livingAttacker.getY() + 1, livingAttacker.getZ(),
                            10, 0.3, 0.5, 0.3, 0.02);
                }
            }
        }

        // Ender Monocle: 20% chance to dodge by short-range teleport
        if (hasCurios && CuriosCompat.isItemEquippedInCurios(player, BrtFacts.ENDER_MONOCLE.get())) {
            if (player.getRandom().nextFloat() < 0.20f) {
                double dx = (player.getRandom().nextDouble() - 0.5) * 8.0;
                double dz = (player.getRandom().nextDouble() - 0.5) * 8.0;
                double newX = player.getX() + dx;
                double newY = player.getY();
                double newZ = player.getZ() + dz;

                if (player.randomTeleport(newX, newY, newZ, true)) {
                    player.level().playSound(null, player.xo, player.yo, player.zo,
                            SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (player.level() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.PORTAL,
                                player.xo, player.yo + 1, player.zo,
                                32, 0.5, 1.0, 0.5, 0.1);
                    }
                    event.setCanceled(true);
                }
            }
        }
    }
}

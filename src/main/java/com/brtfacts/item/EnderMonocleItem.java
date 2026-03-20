package com.brtfacts.item;

import com.brtfacts.integration.curios.CuriosCompat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.ChatFormatting;
import java.util.List;

public class EnderMonocleItem extends Item {

    private static final int GLOW_RADIUS = 24;
    private static final int GLOW_DURATION = 60;

    public EnderMonocleItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (ModList.get().isLoaded("curios")) {
            return CuriosCompat.createCustomTickCurioProvider(stack, (slotContext, itemStack) -> {
                LivingEntity entity = slotContext.entity();
                if (!entity.level().isClientSide() && entity instanceof Player player) {
                    Level level = player.level();
                    // Apply Glowing to all living entities within radius
                    if (player.tickCount % 20 == 0) {
                        AABB area = player.getBoundingBox().inflate(GLOW_RADIUS);
                        List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, area,
                                e -> e != player && e.isAlive());
                        for (LivingEntity target : nearby) {
                            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOW_DURATION, 0, true, false, false));
                        }
                    }
                    // Night vision for the wearer
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 0, false, false, true));
                }
            });
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("item.brtfacts.ender_monocle.desc").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}

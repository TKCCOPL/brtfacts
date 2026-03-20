package com.brtfacts.item;

import com.brtfacts.integration.curios.CuriosCompat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.ChatFormatting;
import java.util.List;

public class MagmaHeartBeltItem extends Item {

    public MagmaHeartBeltItem() {
        super(new Properties().stacksTo(1).fireResistant());
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
                    // Fire Resistance
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 220, 0, false, false, true));
                    // Clear freeze ticks - immune to powder snow
                    if (player.getTicksFrozen() > 0) {
                        player.setTicksFrozen(0);
                    }
                }
            });
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("item.brtfacts.magma_heart_belt.desc").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}

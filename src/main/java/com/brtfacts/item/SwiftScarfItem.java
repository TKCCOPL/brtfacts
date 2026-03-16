package com.brtfacts.item;

import com.brtfacts.integration.curios.CuriosCompat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public class SwiftScarfItem extends Item {

    public SwiftScarfItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (ModList.get().isLoaded("curios")) {
            return CuriosCompat.createEffectCurioProvider(stack,
                () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 220, 0, false, false, true));
        }
        return null;
    }
}

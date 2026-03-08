package com.brtfacts.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BunnySlippersItem extends ArmorItem {
    public BunnySlippersItem() {
        super(ArmorMaterials.LEATHER, Type.BOOTS, new Properties().stacksTo(1));
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide()) {
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 220, 1, false, false, true));
        }
    }
}

package com.brtfacts.integration.curios;

import com.google.common.collect.Multimap;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CuriosCompat {

    public static ICapabilityProvider createEffectCurioProvider(ItemStack stack,
                                                                 Supplier<MobEffectInstance> effectSupplier) {
        return new ICapabilityProvider() {
            private final LazyOptional<ICurio> curio = LazyOptional.of(() -> new ICurio() {
                @Override
                public ItemStack getStack() {
                    return stack;
                }

                @Override
                public void curioTick(SlotContext slotContext) {
                    LivingEntity entity = slotContext.entity();
                    if (!entity.level().isClientSide() && entity instanceof Player player) {
                        player.addEffect(effectSupplier.get());
                    }
                }

                @Override
                public boolean canEquipFromUse(SlotContext slotContext) {
                    return true;
                }
            });

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                return CuriosCapability.ITEM.orEmpty(cap, curio);
            }
        };
    }

    public static ICapabilityProvider createAttributeCurioProvider(ItemStack stack,
                                                                    Multimap<Attribute, AttributeModifier> modifiers) {
        return new ICapabilityProvider() {
            private final LazyOptional<ICurio> curio = LazyOptional.of(() -> new ICurio() {
                @Override
                public ItemStack getStack() {
                    return stack;
                }

                @Override
                public Multimap<Attribute, AttributeModifier> getAttributeModifiers(
                        SlotContext slotContext, UUID uuid) {
                    return modifiers;
                }

                @Override
                public boolean canEquipFromUse(SlotContext slotContext) {
                    return true;
                }
            });

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                return CuriosCapability.ITEM.orEmpty(cap, curio);
            }
        };
    }

    public static ICapabilityProvider createCustomTickCurioProvider(ItemStack stack,
                                                                      BiConsumer<SlotContext, ItemStack> tickHandler) {
        return new ICapabilityProvider() {
            private final LazyOptional<ICurio> curio = LazyOptional.of(() -> new ICurio() {
                @Override
                public ItemStack getStack() {
                    return stack;
                }

                @Override
                public void curioTick(SlotContext slotContext) {
                    tickHandler.accept(slotContext, stack);
                }

                @Override
                public boolean canEquipFromUse(SlotContext slotContext) {
                    return true;
                }
            });

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                return CuriosCapability.ITEM.orEmpty(cap, curio);
            }
        };
    }

    public static boolean isItemEquippedInCurios(Player player, Item item) {
        return CuriosApi.getCuriosInventory(player).map(handler ->
            handler.findFirstCurio(item).isPresent()
        ).orElse(false);
    }
}

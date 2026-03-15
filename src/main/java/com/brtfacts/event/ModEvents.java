package com.brtfacts.event;

import com.brtfacts.BrtFacts;
import com.brtfacts.item.BunnySlippersItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = BrtFacts.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            CuriosApi.getCuriosHelper()
                    .findEquippedCurio(stack -> stack.getItem() instanceof BunnySlippersItem, player)
                    .ifPresent(result -> {
                        event.setDistance(0.0F);
                        event.setCanceled(true);
                    });
        }
    }
}

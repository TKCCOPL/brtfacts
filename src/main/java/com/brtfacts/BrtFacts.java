package com.brtfacts;

import com.brtfacts.item.BunnySlippersItem;
import com.brtfacts.item.IronFistItem;
import com.brtfacts.item.MinersGogglesItem;
import com.brtfacts.item.SwiftScarfItem;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(BrtFacts.MODID)
public class BrtFacts
{
    public static final String MODID = "brtfacts";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> SWIFT_SCARF = ITEMS.register("swift_scarf", SwiftScarfItem::new);
    public static final RegistryObject<Item> MINERS_GOGGLES = ITEMS.register("miners_goggles", MinersGogglesItem::new);
    public static final RegistryObject<Item> IRON_FIST = ITEMS.register("iron_fist", IronFistItem::new);
    public static final RegistryObject<Item> BUNNY_SLIPPERS = ITEMS.register("bunny_slippers", BunnySlippersItem::new);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> BRTFACTS_TAB = CREATIVE_MODE_TABS.register("brtfacts_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> MINERS_GOGGLES.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(SWIFT_SCARF.get());
                output.accept(MINERS_GOGGLES.get());
                output.accept(IRON_FIST.get());
                output.accept(BUNNY_SLIPPERS.get());
            })
            .build());

    public BrtFacts()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            // event.accept(SWIFT_SCARF);
        }
    }
}

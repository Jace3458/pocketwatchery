package net.hexvolt.pocketwatchery.item;

import net.hexvolt.pocketwatchery.Pocketwatchery;
import net.hexvolt.pocketwatchery.item.custom.PocketwatchClockItem;
import net.hexvolt.pocketwatchery.item.custom.PocketwatchGrowthItem;
import net.hexvolt.pocketwatchery.item.custom.PocketwatchYouthItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Pocketwatchery.MODID);

    // Ingredients
    public static final DeferredItem<Item> TIME_GRAIN = ITEMS.registerSimpleItem("grain_of_time");
    public static final DeferredItem<Item> TIME_CRYSTAL = ITEMS.registerSimpleItem("time_crystal");


    // Pocketwatches
    public static final DeferredItem<Item> POCKETWATCH_CLOCK = ITEMS.register(
            "pocketwatch_clock",
            () -> new PocketwatchClockItem(new Item.Properties().stacksTo(1))
    );
    public static final DeferredItem<Item> POCKETWATCH_GROWTH = ITEMS.register(
            "pocketwatch_growth",
            () -> new PocketwatchGrowthItem(new Item.Properties().stacksTo(1))
    );
    public static final DeferredItem<Item> POCKETWATCH_YOUTH = ITEMS.register(
            "pocketwatch_youth",
            () -> new PocketwatchYouthItem(new Item.Properties().stacksTo(1))
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);

    }
}

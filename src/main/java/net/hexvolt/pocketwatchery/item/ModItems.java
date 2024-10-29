package net.hexvolt.pocketwatchery.item;

import net.hexvolt.pocketwatchery.Pocketwatchery;
import net.hexvolt.pocketwatchery.item.custom.PocketwatchBaseItem;
import net.hexvolt.pocketwatchery.item.custom.PocketwatchClockItem;
import net.hexvolt.pocketwatchery.item.custom.PocketwatchGrowthItem;
import net.hexvolt.pocketwatchery.item.custom.PocketwatchYouthItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Pocketwatchery.MODID);

    public static final DeferredRegister.DataComponents COMPONENT_REGISTRAR = DeferredRegister.createDataComponents(
            Registries.DATA_COMPONENT_TYPE, Pocketwatchery.MODID
    );

    // Open/Closed-ness
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PocketwatchBaseItem.PocketwatchClosedRecord>> POCKETWATCH_CLOSED_COMPONENT = COMPONENT_REGISTRAR.registerComponentType(
            "pocketwatch_closed",
            builder -> builder
                    .persistent(PocketwatchBaseItem.PocketwatchClosedRecord.CODEC)
                    .networkSynchronized(PocketwatchBaseItem.PocketwatchClosedRecord.STREAM_CODEC)
    );
    
    // Ingredients
    public static final DeferredItem<Item> TIME_GRAIN = ITEMS.registerSimpleItem("grain_of_time");
    public static final DeferredItem<Item> TIME_CRYSTAL = ITEMS.registerSimpleItem("time_crystal");


    // Pocketwatches
    public static final DeferredItem<Item> POCKETWATCH_CLOCK = ITEMS.register(
            "pocketwatch_clock",
            () -> new PocketwatchClockItem(new Item.Properties().stacksTo(1).durability(20).component(POCKETWATCH_CLOSED_COMPONENT.value(), new PocketwatchBaseItem.PocketwatchClosedRecord(true)))
    );
    public static final DeferredItem<Item> POCKETWATCH_GROWTH = ITEMS.register(
            "pocketwatch_growth",
            () -> new PocketwatchGrowthItem(new Item.Properties().stacksTo(1).durability(80).component(POCKETWATCH_CLOSED_COMPONENT.value(), new PocketwatchBaseItem.PocketwatchClosedRecord(true)))
    );
    public static final DeferredItem<Item> POCKETWATCH_YOUTH = ITEMS.register(
            "pocketwatch_youth",
            () -> new PocketwatchYouthItem(new Item.Properties().stacksTo(1).durability(10).component(POCKETWATCH_CLOSED_COMPONENT.value(), new PocketwatchBaseItem.PocketwatchClosedRecord(true)))
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        COMPONENT_REGISTRAR.register(eventBus);
    }
}

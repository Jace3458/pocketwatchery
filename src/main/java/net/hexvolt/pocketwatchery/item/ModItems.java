package net.hexvolt.pocketwatchery.item;

import net.hexvolt.pocketwatchery.Pocketwatchery;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Pocketwatchery.MODID);

    public static final DeferredItem<Item> POCKETWATCH_GROWTH = ITEMS.register(
            "pocketwatch_growth",
            () -> new Item(new Item.Properties())
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);

    }
}

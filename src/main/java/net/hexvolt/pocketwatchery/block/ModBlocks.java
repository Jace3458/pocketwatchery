package net.hexvolt.pocketwatchery.block;

import net.hexvolt.pocketwatchery.Pocketwatchery;
import net.hexvolt.pocketwatchery.item.ModItems;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Pocketwatchery.MODID);

    public static final DeferredBlock<ColoredFallingBlock> TIME_SAND_BLOCK = registerBlockWithItem(
            "time_sand",
            () -> new ColoredFallingBlock(
                    new ColorRGBA(0xefcef9),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SAND)
            )
    );

    private static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Supplier<T> block) {
        DeferredBlock<T> deferredBlock = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new BlockItem(deferredBlock.get(), new Item.Properties()));
        return deferredBlock;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

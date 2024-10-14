package net.hexvolt.pocketwatchery.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;

public class PocketwatchGrowthItem extends Item {
    public PocketwatchGrowthItem(Properties properties) { super(properties); }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockPos offsetBlock = blockpos.relative(context.getClickedFace());

        if (applyGrowth(context.getItemInHand(), level, blockpos, context.getPlayer())) {
            if (!level.isClientSide) {
                context.getPlayer().gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                level.levelEvent(1505,blockpos,15);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            BlockState blockState = level.getBlockState(blockpos);
            boolean flag = blockState.isFaceSturdy(level, blockpos, context.getClickedFace());
            if (flag && applyWaterGrowth(level, offsetBlock, context.getClickedFace())) {
                if (!level.isClientSide) {
                    context.getPlayer().gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                    level.levelEvent(1505, offsetBlock, 15);
                }

                return InteractionResult.sidedSuccess((level.isClientSide));
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public static boolean applyGrowth(ItemStack itemStack, Level level, BlockPos blockPosition, @Nullable net.minecraft.world.entity.player.Player player) {
        BlockState blockstate = level.getBlockState(blockPosition);
        var event = net.neoforged.neoforge.event.EventHooks.fireBonemealEvent(player, level, blockPosition, blockstate, itemStack);
        if (event.isCanceled()) return event.isSuccessful();
        if (blockstate.getBlock() instanceof BonemealableBlock bonemealableblock && bonemealableblock.isValidBonemealTarget(level, blockPosition, blockstate)) {
            if (level instanceof ServerLevel) {
                if (bonemealableblock.isBonemealSuccess(level, level.random, blockPosition, blockstate)) {
                    bonemealableblock.performBonemeal((ServerLevel)level, level.random, blockPosition, blockstate);
                }
            }
            return true;
        }
        return false;
    }

    public static boolean applyWaterGrowth(Level level, BlockPos pos, @Nullable Direction clickedSide) {
        if (level.getBlockState(pos).is(Blocks.WATER) && level.getFluidState(pos).getAmount() == 8) {
            if (!(level instanceof ServerLevel)) {
                return true;
            } else {
                RandomSource randomsource = level.getRandom();

                label78:
                for (int i = 0; i < 128; i++) {
                    BlockPos blockpos = pos;
                    BlockState blockstate = Blocks.SEAGRASS.defaultBlockState();

                    for (int j = 0; j < i / 16; j++) {
                        blockpos = blockpos.offset(
                                randomsource.nextInt(3) - 1, (randomsource.nextInt(3) - 1) * randomsource.nextInt(3) / 2, randomsource.nextInt(3) - 1
                        );
                        if (level.getBlockState(blockpos).isCollisionShapeFullBlock(level, blockpos)) {
                            continue label78;
                        }
                    }

                    Holder<Biome> holder = level.getBiome(blockpos);
                    if (holder.is(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)) {
                        if (i == 0 && clickedSide != null && clickedSide.getAxis().isHorizontal()) {
                            blockstate = BuiltInRegistries.BLOCK
                                    .getRandomElementOf(BlockTags.WALL_CORALS, level.random)
                                    .map(p_204100_ -> p_204100_.value().defaultBlockState())
                                    .orElse(blockstate);
                            if (blockstate.hasProperty(BaseCoralWallFanBlock.FACING)) {
                                blockstate = blockstate.setValue(BaseCoralWallFanBlock.FACING, clickedSide);
                            }
                        } else if (randomsource.nextInt(4) == 0) {
                            blockstate = BuiltInRegistries.BLOCK
                                    .getRandomElementOf(BlockTags.UNDERWATER_BONEMEALS, level.random)
                                    .map(p_204095_ -> p_204095_.value().defaultBlockState())
                                    .orElse(blockstate);
                        }
                    }

                    if (blockstate.is(BlockTags.WALL_CORALS, blockStateMeMaybe -> blockStateMeMaybe.hasProperty(BaseCoralWallFanBlock.FACING))) {
                        for (int k = 0; !blockstate.canSurvive(level, blockpos) && k < 4; k++) {
                            blockstate = blockstate.setValue(BaseCoralWallFanBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(randomsource));
                        }
                    }

                    if (blockstate.canSurvive(level, blockpos)) {
                        BlockState blockstate1 = level.getBlockState(blockpos);
                        if (blockstate1.is(Blocks.WATER) && level.getFluidState(blockpos).getAmount() == 8) {
                            level.setBlock(blockpos, blockstate, 3);
                        } else if (blockstate1.is(Blocks.SEAGRASS) && randomsource.nextInt(10) == 0) {
                            ((BonemealableBlock)Blocks.SEAGRASS).performBonemeal((ServerLevel)level, randomsource, blockpos, blockstate1);
                        }
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }
}

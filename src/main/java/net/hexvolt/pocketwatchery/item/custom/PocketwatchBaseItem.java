package net.hexvolt.pocketwatchery.item.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.hexvolt.pocketwatchery.ModSounds;
import net.hexvolt.pocketwatchery.item.ModItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PocketwatchBaseItem extends Item {

    public PocketwatchBaseItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!isOpen(player, usedHand)) {
            setOpen(level, player, usedHand, true);
        }
        else if (!player.isSecondaryUseActive()) {
            setOpen(level, player, usedHand, false);
        }
        return super.use(level, player, usedHand);
    }

    protected void setOpen(Level level, Player player, InteractionHand usedHand, boolean newOpen) {
        ItemStack heldItem = player.getItemInHand(usedHand);
        if (!(heldItem.getItem() instanceof PocketwatchBaseItem)) {
            return;
        }

        if (newOpen != isOpen(player.getItemInHand(usedHand))) {
            if (level.isClientSide) {
            } else {
                heldItem.set(ModItems.POCKETWATCH_CLOSED_COMPONENT.get(), new PocketwatchClosedRecord(!newOpen));
                level.playSound(
                        null,
                        player.getX(), player.getY(), player.getZ(),
                        newOpen ? ModSounds.POCKETWATCH_OPEN.get() : ModSounds.POCKETWATCH_CLOSE.get(),
                        SoundSource.BLOCKS,
                        1.0F, 1.0F
                );
            }
        }
    }

    public static boolean isOpen(Player player, InteractionHand hand) {
        return isOpen(player.getItemInHand(hand));
    }

    public static boolean isOpen(ItemStack stack) {
        PocketwatchClosedRecord closedRecord = stack.get(ModItems.POCKETWATCH_CLOSED_COMPONENT.get());
        return !(closedRecord != null && closedRecord.closed);
    }

    protected int getDurabilityUse() { return 1; }

    protected void consumeTime(Player player, InteractionHand hand) {
        ItemStack pocketwatch = player.getItemInHand(hand);
        if (canUsePocketwatch(player, hand)) {
            pocketwatch.hurtAndBreak(getDurabilityUse(), player, LivingEntity.getSlotForHand(hand));
        }
    }

    public static boolean canUsePocketwatch(Player player, InteractionHand usedHand) {
        ItemStack pocketwatch = player.getItemInHand(usedHand);
        return canUsePocketwatch(player, pocketwatch);
    }

    public static boolean canUsePocketwatch(Player player, ItemStack pocketwatch) {
        return player.isSecondaryUseActive() && isOpen(pocketwatch) && pocketwatch.getDamageValue() < pocketwatch.getMaxDamage() - 1;
    }

    public static float predicatize(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        // needs to return 1 if closed
        return !isOpen(stack) ? 1.0f : 0.0f;
    }

    public record PocketwatchClosedRecord(boolean closed) {
        public static final Codec<PocketwatchClosedRecord> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                    Codec.BOOL.fieldOf("closed").forGetter(PocketwatchClosedRecord::closed)
                ).apply(instance, PocketwatchClosedRecord::new)
        );

        public static final StreamCodec<ByteBuf, PocketwatchClosedRecord> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                PocketwatchClosedRecord::closed,
                PocketwatchClosedRecord::new
        );
    }
}

package net.hexvolt.pocketwatchery.item.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.hexvolt.pocketwatchery.ModSounds;
import net.hexvolt.pocketwatchery.item.ModItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
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
    private PatchedDataComponentMap patchedComponents;

    public PocketwatchBaseItem(Properties properties) {
        super(properties);
        this.patchedComponents = PatchedDataComponentMap.fromPatch(components(), DataComponentPatch.EMPTY);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if (isOpen()) {
            if (player.isSecondaryUseActive()) {
                setOpen(level, player, false);
            }
        } else {
            setOpen(level, player, true);
        }

        return super.use(level, player, usedHand);
    }

    protected void setOpen(Level level, Player player, boolean newOpen) {
        if (newOpen != isOpen()) {
            if (level.isClientSide) {
            } else {
                patchedComponents.set(ModItems.POCKETWATCH_CLOSED_COMPONENT.get(), new PocketwatchClosedRecord(!newOpen));
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

    public boolean isOpen() {
        PocketwatchClosedRecord closedRecord = patchedComponents.get(ModItems.POCKETWATCH_CLOSED_COMPONENT.get());
        return !(closedRecord != null && closedRecord.closed);
    }

    protected int getDurabilityUse() { return 1; }

    protected void consumeTime(Player player, InteractionHand hand) {
        ItemStack pocketwatch = player.getItemInHand(hand);
        if (canUsePocketwatch(pocketwatch)) {
            pocketwatch.hurtAndBreak(getDurabilityUse(), player, LivingEntity.getSlotForHand(hand));
        }
    }

    public boolean canUsePocketwatch(Player player, InteractionHand usedHand) {
        ItemStack pocketwatch = player.getItemInHand(usedHand);
        return canUsePocketwatch(pocketwatch);
    }

    public boolean canUsePocketwatch(ItemStack pocketwatch) {
        return isOpen() && pocketwatch.getDamageValue() < pocketwatch.getMaxDamage() - 1;
    }

    public static float predicatize(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        if (stack.getItem() instanceof PocketwatchBaseItem pocketwatchItem) {
            PocketwatchClosedRecord closedRecord = pocketwatchItem.patchedComponents.get(ModItems.POCKETWATCH_CLOSED_COMPONENT.get());
            return closedRecord != null && closedRecord.closed ? 1.0f : 0.0f;
        } else {
            return 0.0f;
        }
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

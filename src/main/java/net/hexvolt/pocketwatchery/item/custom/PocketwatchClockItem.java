package net.hexvolt.pocketwatchery.item.custom;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PocketwatchClockItem extends PocketwatchBaseItem {
    public PocketwatchClockItem(Properties properties) { super(properties); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (canUsePocketwatch(player, hand)) {
            String message = getFormattedTime(world);
            if (world.isClientSide) {
                player.sendSystemMessage(Component.literal(message));
            }
            if (!world.isClientSide) {
                consumeTime(player, hand);
            }
        }

        return super.use(world, player, hand);
    }

    @NotNull
    private static String getFormattedTime(Level world) {
        long tickTime = world.getDayTime();
        if (tickTime < 18000L) {
            tickTime = tickTime + 6000;
        } else {
            tickTime = tickTime - 18000;
        }

        String hours = String.format("%02d", tickTime / 1000);
        String minutes = String.format("%02.0f",  (tickTime % 1000) / 16.66666);
        if (minutes.equals("60")) {
            minutes = "59";
        }


        // Change to open pocketwatch skin
        // Small delay
        // Print time
        // Close pocketwatch

        return hours + ":" + minutes;
    }
}

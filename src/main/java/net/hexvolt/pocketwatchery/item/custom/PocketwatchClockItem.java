package net.hexvolt.pocketwatchery.item.custom;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PocketwatchClockItem extends PocketwatchBaseItem {
    public PocketwatchClockItem(Properties properties) { super(properties); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!canUsePocketwatch(player, hand)) { return super.use(world, player, hand); }

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

        String message = hours + ":" + minutes;
        if (world.isClientSide) {
            player.sendSystemMessage(Component.literal(message));
        }
        return super.use(world, player, hand);
    }
}

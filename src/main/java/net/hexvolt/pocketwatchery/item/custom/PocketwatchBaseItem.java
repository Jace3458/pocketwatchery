package net.hexvolt.pocketwatchery.item.custom;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@MethodsReturnNonnullByDefault
public class PocketwatchBaseItem extends Item {
    public PocketwatchBaseItem(Properties properties) { super(properties); }

    protected int getDurabilityUse() { return 1; }

    protected void usePocketwatch(Player player, InteractionHand hand) {
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
        return pocketwatch.getDamageValue() < pocketwatch.getMaxDamage() - 1;
    }
}

package net.hexvolt.pocketwatchery.item.custom;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PocketwatchOverclockItem extends PocketwatchBaseItem {

    public PocketwatchOverclockItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (canUsePocketwatch(player, usedHand) && !level.isClientSide) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 300, 0));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 1));
            consumeTime(player, usedHand);
        }
        return super.use(level, player, usedHand);
    }
}

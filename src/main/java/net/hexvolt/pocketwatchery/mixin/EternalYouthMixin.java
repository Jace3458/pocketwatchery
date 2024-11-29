package net.hexvolt.pocketwatchery.mixin;

import net.hexvolt.pocketwatchery.Pocketwatchery;
import net.hexvolt.pocketwatchery.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableMob.class)
public abstract class EternalYouthMixin extends PathfinderMob {
    @Shadow
    protected int age;

    protected EternalYouthMixin(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "setAge", cancellable = true)
    // Prevent young mobs from aging if they been young pocketwatched
    private void setAge(int age, CallbackInfo ci) {
        if (this.hasData(Pocketwatchery.ETERNALLY_YOUNG)) {
            this.age = this.getData(Pocketwatchery.ETERNALLY_YOUNG);
            ci.cancel();
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(ModItems.POCKETWATCH_YOUTH) && this.isBaby()) {
            player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            this.setData(Pocketwatchery.ETERNALLY_YOUNG, this.age);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(player, hand);
        }
    }
}

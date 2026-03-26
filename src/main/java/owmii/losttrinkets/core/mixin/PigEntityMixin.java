package owmii.losttrinkets.core.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.item.Itms;

@Mixin(Pig.class)
public abstract class PigEntityMixin {
    @Inject(method = "getControllingPassenger", at = @At("RETURN"), cancellable = true)
    private void getControllingPassenger(CallbackInfoReturnable<LivingEntity> cir) {
        if (cir.getReturnValue() == null && (Object) this instanceof Pig pig) {
            Entity passenger = pig.getFirstPassenger();
            if (passenger instanceof Player player) {
                boolean hasCarrot = player.getMainHandItem().is(Items.CARROT_ON_A_STICK) || player.getOffhandItem().is(Items.CARROT_ON_A_STICK);
                if (!hasCarrot && LostTrinketsAPI.getTrinkets(player).isActive(Itms.PIGGY)) {
                    cir.setReturnValue(player);
                }
            }
        }
    }

    @Inject(method = "getRiddenSpeed", at = @At("HEAD"), cancellable = true)
    private void getRiddenSpeed(Player player, CallbackInfoReturnable<Float> cir) {
        if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.PIGGY) && (Object) this instanceof Pig pig) {
            cir.setReturnValue((float) pig.getAttributeValue(Attributes.MOVEMENT_SPEED) * 3.5F);
        }
    }
}

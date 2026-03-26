package owmii.losttrinkets.core.mixin;

import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owmii.losttrinkets.handler.TargetHandler;

@Mixin(TargetingConditions.class)
public class EntityPredicateMixin {
    @Inject(method = "test", at = @At("TAIL"), cancellable = true)
    public void canTarget(LivingEntity attacker, LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            if (TargetHandler.preventTargeting(attacker, target)) {
                cir.setReturnValue(false);
            }
        }
    }
}

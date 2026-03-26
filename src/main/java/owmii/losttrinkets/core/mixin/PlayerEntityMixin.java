package owmii.losttrinkets.core.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owmii.losttrinkets.item.trinkets.ThaSpiderTrinket;

@Mixin(LivingEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "onClimbable", at = @At("TAIL"), cancellable = true)
    private void onClimbable(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() && (Object) this instanceof Player player && ThaSpiderTrinket.doClimb(player)) {
            cir.setReturnValue(true);
        }
    }
}

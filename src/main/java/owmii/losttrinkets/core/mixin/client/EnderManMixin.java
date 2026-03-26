package owmii.losttrinkets.core.mixin.client;

import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.item.Itms;

@Mixin(EnderMan.class)
public abstract class EnderManMixin {
    @Inject(method = "isLookingAtMe", at = @At("HEAD"), cancellable = true)
    private void losttrinkets$blankEyes(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.BLANK_EYES)) {
            cir.setReturnValue(false);
        }
    }
}

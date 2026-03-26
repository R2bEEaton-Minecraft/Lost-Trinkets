package owmii.losttrinkets.core.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.item.Itms;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    private void losttrinkets$showInvisibleEntitiesToMindsEye(Player player, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        Player localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null || player != localPlayer) {
            return;
        }
        if (!(entity instanceof LivingEntity living) || !living.isInvisible()) {
            return;
        }
        if (LostTrinketsAPI.getTrinkets(localPlayer).isActive(Itms.MINDS_EYE)) {
            cir.setReturnValue(false);
        }
    }
}

package owmii.losttrinkets.core.mixin;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

@Mixin(Villager.class)
public class VillagerEntityMixin {
    @Inject(method = "getPlayerReputation", at = @At("TAIL"), cancellable = true)
    public void getPlayerReputation(Player player, CallbackInfoReturnable<Integer> cir) {
        Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
        if (trinkets.isActive(Itms.KARMA)) {
            cir.setReturnValue(cir.getReturnValueI() + 100);
        }
    }

    @Inject(method = "updateSpecialPrices", at = @At("TAIL"))
    private void updateSpecialPrices(Player player, CallbackInfo ci) {
        Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
        if (trinkets.isActive(Itms.KARMA) && (Object) this instanceof Villager villager) {
            for (MerchantOffer offer : villager.getOffers()) {
                int baseCost = offer.getBaseCostA().getCount();
                offer.addToSpecialPriceDiff(-Math.max(2, (int) Math.ceil(baseCost * 0.4D)));
            }
        }
    }
}

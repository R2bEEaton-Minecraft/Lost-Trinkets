package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class ThaSpiderTrinket extends Trinket<ThaSpiderTrinket> {
    public ThaSpiderTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static boolean doClimb(LivingEntity entity) {
        if (entity instanceof Player player) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.THA_SPIDER)) {
                return entity.horizontalCollision;
            }
        }
        return false;
    }

    public static void tick(Player player) {
        if (!doClimb(player)) {
            return;
        }
        player.resetFallDistance();
        if (player.zza > 0.0F) {
            player.setDeltaMovement(player.getDeltaMovement().x, 0.2D, player.getDeltaMovement().z);
        } else if (player.isShiftKeyDown()) {
            player.setDeltaMovement(player.getDeltaMovement().x, -0.15D, player.getDeltaMovement().z);
        } else if (player.getDeltaMovement().y < 0.0D) {
            player.setDeltaMovement(player.getDeltaMovement().x, 0.0D, player.getDeltaMovement().z);
        }
    }
}

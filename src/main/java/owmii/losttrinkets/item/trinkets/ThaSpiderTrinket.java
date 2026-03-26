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
        if (entity instanceof PlayerEntity) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets((PlayerEntity) entity);
            if (trinkets.isActive(Itms.THA_SPIDER)) {
                return entity.collidedHorizontally;
            }
        }
        return false;
    }
}

package owmii.losttrinkets.item.trinkets;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

public class BlazeHeartTrinket extends Trinket<BlazeHeartTrinket> {
    public BlazeHeartTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static boolean isImmuneToFire(LivingEntity target, DamageSource source) {
        if (target instanceof Player player) {
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.BLAZE_HEART)) {
                if (source.is(DamageTypeTags.IS_FIRE)) {
                    player.clearFire();
                    return true;
                }
            }
        }
        return false;
    }
}

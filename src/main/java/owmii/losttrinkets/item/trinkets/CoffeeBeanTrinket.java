package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class CoffeeBeanTrinket extends Trinket<CoffeeBeanTrinket> {
    public CoffeeBeanTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onPotion(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.COFFEE_BEAN)) {
                MobEffect effect = event.getEffectInstance().getEffect();
                if (effect.equals(MobEffects.CONFUSION) || effect.equals(MobEffects.DIG_SLOWDOWN) || effect.equals(MobEffects.MOVEMENT_SLOWDOWN)) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}

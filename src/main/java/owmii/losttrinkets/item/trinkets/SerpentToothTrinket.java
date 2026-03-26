package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class SerpentToothTrinket extends Trinket<SerpentToothTrinket> {
    public SerpentToothTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onHurt(LivingHurtEvent event) {
        Entity entity = event.getSource().getDirectEntity();
        if (entity instanceof Player player) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.SERPENT_TOOTH)) {
                event.getEntity().addEffect(new MobEffectInstance(MobEffects.POISON, 120, 0));
            }
        }
    }
}

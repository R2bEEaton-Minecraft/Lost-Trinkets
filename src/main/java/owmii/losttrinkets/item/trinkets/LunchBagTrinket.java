package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class LunchBagTrinket extends Trinket<LunchBagTrinket> {
    public LunchBagTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onUseFinish(LivingEntityUseItemEvent.Finish event) {
        LivingEntity entity = event.getEntity();
        Level world = entity.level();
        if (entity instanceof Player player) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (event.getItem().isEdible()) {
                FoodProperties food = event.getItem().getFoodProperties(entity);
                if (food != null && food.getEffects().isEmpty()) {
                    if (trinkets.isActive(Itms.LUNCH_BAG) && world.random.nextInt(10) == 0) {
                        player.addEffect(new MobEffectInstance(MobEffects.SATURATION, world.random.nextInt(200) + 100, 1, false, false));
                    }
                }
            }
        }
    }
}

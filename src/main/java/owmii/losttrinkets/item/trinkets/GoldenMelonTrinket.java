package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class GoldenMelonTrinket extends Trinket<GoldenMelonTrinket> {
    public GoldenMelonTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onUseFinish(LivingEntityUseItemEvent.Finish event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (event.getItem().isEdible()) {
                FoodProperties food = event.getItem().getFoodProperties(entity);
                if (food != null && food.getEffects().isEmpty()) {
                    if (trinkets.isActive(Itms.GOLDEN_MELON)) {
                        player.heal(food.getNutrition());
                    }
                }
            }
        }
    }
}

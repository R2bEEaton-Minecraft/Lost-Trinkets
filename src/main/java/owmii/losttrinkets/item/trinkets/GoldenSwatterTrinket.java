package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class GoldenSwatterTrinket extends Trinket<GoldenSwatterTrinket> {
    public GoldenSwatterTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onHurt(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        if (living instanceof Silverfish || living instanceof Endermite) {
            Entity entity = event.getSource().getDirectEntity();
            if (entity instanceof Player player) {
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                if (trinkets.isActive(Itms.GOLDEN_SWATTER)) {
                    if (living.isNonBoss()) {
                        living.setHealth(0.5F);
                    }
                }
            }
        }
    }
}

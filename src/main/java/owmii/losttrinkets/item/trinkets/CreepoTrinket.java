package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

public class CreepoTrinket extends Trinket {
    public CreepoTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void resetExplosion(CriticalHitEvent event) {
        if (LostTrinketsAPI.getTrinkets(event.getPlayer()).isActive(Itms.CREEPO)) {
            Entity target = event.getTarget();
            if (target instanceof Creeper creeper) {
                creeper.swell = 0;
                creeper.oldSwell = 0;
            }
        }
    }
}

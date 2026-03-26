package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;
import owmii.losttrinkets.api.trinket.ITargetingTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class ThaGhostTrinket extends Trinket<ThaGhostTrinket> implements ITargetingTrinket {
    public ThaGhostTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public boolean preventTargeting(Mob mob, Player player, boolean notAttacked) {
        return player.hasEffect(MobEffects.INVISIBILITY);
    }
}

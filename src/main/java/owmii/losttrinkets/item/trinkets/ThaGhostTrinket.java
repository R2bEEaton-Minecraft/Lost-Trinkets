package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.MobEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.potion.Effects;
import owmii.losttrinkets.api.trinket.ITargetingTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class ThaGhostTrinket extends Trinket<ThaGhostTrinket> implements ITargetingTrinket {
    public ThaGhostTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public boolean preventTargeting(MobEntity mob, PlayerEntity player, boolean notAttacked) {
        return notAttacked && player.isPotionActive(Effects.INVISIBILITY);
    }
}

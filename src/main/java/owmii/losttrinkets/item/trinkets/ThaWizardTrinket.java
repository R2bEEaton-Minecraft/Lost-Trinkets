package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.MobEntity;
import net.minecraft.world.entity.monster.WitchEntity;
import net.minecraft.world.entity.player.Player;
import owmii.losttrinkets.api.trinket.ITargetingTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class ThaWizardTrinket extends Trinket<ThaWizardTrinket> implements ITargetingTrinket {
    public ThaWizardTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public boolean preventTargeting(MobEntity mob, PlayerEntity player, boolean notAttacked) {
        return mob instanceof WitchEntity;
    }
}

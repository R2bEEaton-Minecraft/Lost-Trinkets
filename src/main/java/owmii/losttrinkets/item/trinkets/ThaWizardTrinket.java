package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import owmii.losttrinkets.api.trinket.ITargetingTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class ThaWizardTrinket extends Trinket<ThaWizardTrinket> implements ITargetingTrinket {
    public ThaWizardTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public boolean preventTargeting(Mob mob, Player player, boolean notAttacked) {
        return mob instanceof Witch;
    }
}

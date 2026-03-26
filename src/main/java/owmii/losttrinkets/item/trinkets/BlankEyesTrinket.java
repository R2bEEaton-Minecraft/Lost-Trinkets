package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import owmii.losttrinkets.api.trinket.ITargetingTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class BlankEyesTrinket extends Trinket<BlankEyesTrinket> implements ITargetingTrinket {
    public BlankEyesTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @Override
    public boolean preventTargeting(Mob mob, Player player, boolean notAttacked) {
        return mob instanceof EnderMan;
    }
}

package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import owmii.losttrinkets.api.trinket.ITargetingTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.entity.ai.BigFootGoal;

public class BigFootTrinket extends Trinket<BigFootTrinket> implements ITargetingTrinket {
    public BigFootTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void addAvoidGoal(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof PathfinderMob mob) {
            mob.goalSelector.addGoal(-1, new BigFootGoal(mob));
        }
    }

    public boolean preventTargeting(Mob mob, Player player, boolean notAttacked) {
        return mob.isBaby();
    }
}

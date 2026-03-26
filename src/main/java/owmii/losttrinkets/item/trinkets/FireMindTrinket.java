package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.handler.TargetHandler;
import owmii.losttrinkets.item.Itms;

public class FireMindTrinket extends Trinket<FireMindTrinket> {
    public FireMindTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Mob mob) {
            LivingEntity target = mob.getTarget();
            if (target == null) {
                target = TargetHandler.getBrainMemorySafe(mob.getBrain(), MemoryModuleType.ATTACK_TARGET).orElse(null);
            }
            if (target instanceof Player player) {
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                if (trinkets.isActive(Itms.FIRE_MIND) && !mob.isImmuneToFire()) {
                    mob.setSecondsOnFire(3);
                }
            }
        }
    }
}

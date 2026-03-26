package owmii.losttrinkets.handler;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.losttrinkets.api.LostTrinketsAPI;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber
public class TargetHandler {
    public static boolean preventTargeting(LivingEntity attacker, @Nullable LivingEntity target) {
        if (attacker instanceof Mob mob && target instanceof Player player) {
            if (mob instanceof EnderDragon || mob instanceof WitherBoss) {
                return false;
            }
            boolean notAttacked = !player.equals(mob.getLastHurtByMob()) && !player.equals(mob.getLastHurtMob());
            return LostTrinketsAPI.getTrinkets(player).getTargeting().stream()
                    .anyMatch(trinket -> trinket.preventTargeting(mob, player, notAttacked));
        }
        return false;
    }

    public static <T> Optional<T> getBrainMemorySafe(Brain<?> brain, MemoryModuleType<T> type) {
        return brain.hasMemoryValue(type) ? brain.getMemory(type) : Optional.empty();
    }

    @SubscribeEvent
    public static void setTarget(LivingChangeTargetEvent event) {
        LivingEntity living = event.getEntity();
        if (living instanceof Mob mob && preventTargeting(living, event.getNewTarget())) {
            mob.setTarget(null);
            event.setNewTarget(null);
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity living = event.getEntity();
        if (living instanceof Mob mob) {
            if (mob instanceof NeutralMob angerable && mob.level() instanceof ServerLevel serverLevel) {
                UUID targetUUID = angerable.getPersistentAngerTarget();
                if (targetUUID != null && preventTargeting(mob, serverLevel.getPlayerByUUID(targetUUID))) {
                    angerable.stopBeingAngry();
                }
            }
            if (preventTargeting(mob, mob.getTarget())) {
                mob.setTarget(null);
            }
            Brain<?> brain = mob.getBrain();
            getBrainMemorySafe(brain, MemoryModuleType.ATTACK_TARGET).ifPresent(target -> {
                if (preventTargeting(mob, target)) {
                    brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
                }
            });
        }
    }
}

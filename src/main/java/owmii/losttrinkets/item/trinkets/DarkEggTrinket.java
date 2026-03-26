package owmii.losttrinkets.item.trinkets;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.entity.DarkVexEntity;
import owmii.losttrinkets.entity.Entities;
import owmii.losttrinkets.item.Itms;

public class DarkEggTrinket extends Trinket<DarkEggTrinket> {
    public DarkEggTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        Level world = entity.level();
        DamageSource source = event.getSource();
        Entity trueSource = source.getEntity();
        if (entity instanceof Player player) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trueSource instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) trueSource;
                if (trinkets.isActive(Itms.DARK_EGG)) {
                    int entities = world.getEntitiesOfClass(DarkVexEntity.class, new AABB(player.blockPosition()).inflate(16.0D)).size();
                    if (entities < 6 && world instanceof ServerLevel serverLevel) {
                        for (int i = 0; i < 3; i++) {
                            DarkVexEntity vex = Entities.DARK_VEX.get().create(world);
                            if (vex != null) {
                                vex.finalizeSpawn(serverLevel, world.getCurrentDifficultyAt(player.blockPosition()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, null);
                                vex.setTarget(living);
                                vex.setOwner(player);
                                vex.setLastHurtByMob(null);
                                vex.setBoundOrigin(BlockPos.containing(player.position()));
                                vex.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
                                world.addFreshEntity(vex);
                            }
                        }
                    }
                }
            }
        }
    }
}

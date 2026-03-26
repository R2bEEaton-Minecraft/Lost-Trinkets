package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

import java.util.List;

public class MadPiggyTrinket extends Trinket<MadPiggyTrinket> {
    public MadPiggyTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        Level world = entity.level();
        Entity trueSource = event.getSource().getEntity();
        if (entity instanceof Player player) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trueSource instanceof LivingEntity living) {
                if (trinkets.isActive(Itms.MAD_PIGGY)) {
                    AABB bb = new AABB(player.blockPosition()).inflate(24.0D);
                    List<ZombifiedPiglin> entities = world.getEntitiesOfClass(ZombifiedPiglin.class, bb);
                    for (ZombifiedPiglin zombifiedPiglin : entities) {
                        zombifiedPiglin.setTarget(living);
                        world.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.ZOMBIFIED_PIGLIN_ANGRY, SoundSource.HOSTILE, 1.5F, 1.0F);
                    }
                }
            }
        }
    }
}

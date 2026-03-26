package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class MirrorShardTrinket extends Trinket<MirrorShardTrinket> {
    private static final ThreadLocal<Boolean> dealingMirrorDamage = ThreadLocal.withInitial(() -> false);

    public MirrorShardTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onHurt(LivingHurtEvent event) {
        if (dealingMirrorDamage.get()) return;
        try {
            dealingMirrorDamage.set(true);
            mirrorDamage(event);
        } finally {
            dealingMirrorDamage.set(false);
        }
    }

    private static void mirrorDamage(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        Entity trueSource = source.getEntity();
        if (entity instanceof Player player) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trueSource instanceof LivingEntity living) {
                if (trinkets.isActive(Itms.MIRROR_SHARD)) {
                    living.hurt(player.damageSources().playerAttack(player), event.getAmount() / 2.0F);
                }
            }
        }
    }
}

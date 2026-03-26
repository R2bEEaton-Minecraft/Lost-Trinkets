package owmii.losttrinkets.item.trinkets;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class TeaLeafTrinket extends Trinket<TeaLeafTrinket> {
    public TeaLeafTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onPotion(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.TEA_LEAF)) {
                MobEffect effect = event.getEffectInstance().getEffect();
                if (effect.equals(MobEffects.WITHER) || effect.equals(MobEffects.POISON)) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @Override
    public void onActivated(Level world, BlockPos pos, Player player) {
        if (world.isClientSide) {
            return;
        }
        player.removeEffect(MobEffects.POISON);
        player.removeEffect(MobEffects.WITHER);
    }
}

package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class SlingshotTrinket extends Trinket<SlingshotTrinket> {
    public SlingshotTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onHurt(LivingHurtEvent event) {
        Entity entity = event.getSource().getDirectEntity();
        if (entity instanceof Player player) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.SLINGSHOT)) {
                float yRot = player.getYRot() * ((float) Math.PI / 180F);
                event.getEntity().knockback(1.4F, (double) Mth.sin(yRot), (double) (-Mth.cos(yRot)));
            }
        }
    }
}

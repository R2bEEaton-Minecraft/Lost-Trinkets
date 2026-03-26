package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

import java.util.List;

@Mod.EventBusSubscriber
public class StickyMindTrinket extends Trinket<StickyMindTrinket> {
    public StickyMindTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @SubscribeEvent
    public static void onEnderTeleport(EntityTeleportEvent.EnderEntity event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            AABB bb = new AABB(entity.blockPosition()).inflate(16.0D);
            List<Player> players = entity.level().getEntitiesOfClass(Player.class, bb);
            for (Player player : players) {
                if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.STICKY_MIND)) {
                    event.setCanceled(true);
                    break;
                }
            }
        }
    }
}

package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

@Mod.EventBusSubscriber
public class EmptyAmuletTrinket extends Trinket<EmptyAmuletTrinket> {
    public EmptyAmuletTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @SubscribeEvent
    public static void onPearlImpact(ProjectileImpactEvent event) {
        Entity projectile = event.getProjectile();
        if (!(projectile instanceof ThrownEnderpearl pearl) || projectile.level().isClientSide) {
            return;
        }
        Entity owner = pearl.getOwner();
        if (!(owner instanceof Player player)) {
            return;
        }
        if (!LostTrinketsAPI.getTrinkets(player).isActive(Itms.EMPTY_AMULET) || player.getAbilities().instabuild) {
            return;
        }
        ItemStack pearlStack = new ItemStack(Items.ENDER_PEARL);
        if (!player.addItem(pearlStack)) {
            player.drop(pearlStack, false);
        }
    }
}

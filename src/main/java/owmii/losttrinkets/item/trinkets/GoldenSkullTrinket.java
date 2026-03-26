package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

public class GoldenSkullTrinket extends Trinket<GoldenSkullTrinket> {
    public GoldenSkullTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onDrops(LivingDropsEvent event) {
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof Player player) {
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.GOLDEN_SKULL)) {
                LivingEntity target = event.getEntity();
                if (target instanceof Monster) {
                    if (target.level().random.nextInt(20) == 0) {
                        event.getDrops().add(new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(), new ItemStack(Itms.TREASURE_BAG.get())));
                    }
                }
            }
        }
    }
}

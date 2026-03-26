package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

public class ButchersCleaverTrinket extends Trinket<ButchersCleaverTrinket> {
    public ButchersCleaverTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void dropExtra(LivingDropsEvent event) {
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof Player player) {
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.BUTCHERS_CLEAVER)) {
                LivingEntity target = event.getEntity();
                if (target instanceof Animal) {
                    if (target.level().random.nextInt(10) == 0) {
                        ItemStack stack = new ItemStack(Items.BONE, target.level().random.nextInt(2) + 1);
                        event.getDrops().add(new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(), stack));
                    }
                }
            }
        }
    }
}

package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

public class IceShardTrinket extends Trinket<IceShardTrinket> {
    public IceShardTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void frostWalk(LivingEntity entity, BlockPos pos) {
        if (entity instanceof Player player) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.ICE_SHARD)) {
                FrostWalkerEnchantment.onEntityMoved(player, entity.level(), pos, 1);
            }
        }
    }
}

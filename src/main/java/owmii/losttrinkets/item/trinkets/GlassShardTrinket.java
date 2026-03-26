package owmii.losttrinkets.item.trinkets;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.items.ItemHandlerHelper;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.item.Itms;

public final class GlassShardTrinket {
    private GlassShardTrinket() {
    }

    public static void tick(Player player) {
        if (player.level().isClientSide || !LostTrinketsAPI.getTrinkets(player).isActive(Itms.GLASS_SHARD)) {
            return;
        }
        Level level = player.level();
        for (BlockPos pos : BlockPos.betweenClosed(player.blockPosition().below(), player.blockPosition().above())) {
            if (level.getBlockState(pos).is(Blocks.COBWEB)) {
                level.destroyBlock(pos, false);
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.STRING, 1 + level.random.nextInt(2)));
            }
        }
    }
}

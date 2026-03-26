package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import owmii.losttrinkets.api.trinket.ITickableTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class HorseshoeTrinket extends Trinket<HorseshoeTrinket> implements ITickableTrinket {
    public HorseshoeTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @Override
    public void tick(World world, BlockPos pos, PlayerEntity player) {
        player.stepHeight = player.isSneaking() ? 0.6F : 1.0F;
    }

    @Override
    public void onDeactivated(World world, BlockPos pos, PlayerEntity player) {
        super.onDeactivated(world, pos, player);
        player.stepHeight = 0.6F;
    }
}

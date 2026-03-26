package owmii.losttrinkets.item.trinkets;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import owmii.losttrinkets.api.trinket.ITickableTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class HorseshoeTrinket extends Trinket<HorseshoeTrinket> implements ITickableTrinket {
    public HorseshoeTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @Override
    public void tick(Level world, BlockPos pos, Player player) {
        player.setMaxUpStep(player.isShiftKeyDown() ? 0.6F : 1.0F);
    }

    @Override
    public void onDeactivated(Level world, BlockPos pos, Player player) {
        super.onDeactivated(world, pos, player);
        player.setMaxUpStep(0.6F);
    }
}

package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import owmii.losttrinkets.api.trinket.ITickableTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class TurtleShellTrinket extends Trinket<TurtleShellTrinket> implements ITickableTrinket {
    public TurtleShellTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @Override
    public void tick(Level world, BlockPos pos, Player player) {
        player.setAirSupply(player.getMaxAirSupply());
    }
}

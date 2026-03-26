package owmii.losttrinkets.item.trinkets;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import owmii.losttrinkets.api.trinket.ITickableTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class PiggyTrinket extends Trinket<PiggyTrinket> implements ITickableTrinket {
    public PiggyTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @Override
    public void tick(Level world, BlockPos pos, Player player) {
        if (!(player.getVehicle() instanceof Pig pig) || !pig.isSaddled()) {
            return;
        }

        pig.setYRot(player.getYRot());
        pig.setXRot(player.getXRot() * 0.5F);
        pig.yRotO = pig.yBodyRot = pig.yHeadRot = pig.getYRot();
    }
}

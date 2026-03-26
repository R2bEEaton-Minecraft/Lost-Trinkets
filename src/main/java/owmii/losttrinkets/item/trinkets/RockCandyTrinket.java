package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import owmii.losttrinkets.api.trinket.ITickableTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class RockCandyTrinket extends Trinket<RockCandyTrinket> implements ITickableTrinket {
    public RockCandyTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @Override
    public void tick(World world, BlockPos pos, PlayerEntity player) {
        if (!player.isSneaking() && player.moveForward > 0F) {
            player.moveRelative(player.isSprinting() ? 0.22F : 0.15F, new Vector3d(0.0D, 0.0D, 1.0D));
        }
    }
}

package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import owmii.lib.util.math.V3d;
import owmii.losttrinkets.api.trinket.ITickableTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

public class ThaCloudTrinket extends Trinket<ThaCloudTrinket> implements ITickableTrinket {
    public ThaCloudTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @Override
    public void tick(World world, BlockPos pos, PlayerEntity player) {
        if (player.fallDistance > 3.0F) {
            if (!world.isAirBlock(player.getPosition().down(3))) {
                Vector3d v3d = player.getMotion();
                player.setMotion(v3d.x, 0.0D, v3d.z);
                if (world.isRemote) {
                    for (V3d v3d1 : V3d.from(player.getPositionVec()).circled(8, 0.3D)) {
                        world.addParticle(ParticleTypes.CLOUD, v3d1.x, v3d1.y, v3d1.z, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
            player.fallDistance = 0.0F;
        }
    }
}

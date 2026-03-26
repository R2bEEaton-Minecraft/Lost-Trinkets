package owmii.losttrinkets.item.trinkets;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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

        float forwardInput = player.zza;
        if (forwardInput == 0.0F) {
            return;
        }

        double speed = pig.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1.35D;
        if (forwardInput < 0.0F) {
            speed *= 0.35D;
        }
        Vec3 forward = Vec3.directionFromRotation(0.0F, player.getYRot()).scale(speed * forwardInput);
        pig.setDeltaMovement(forward.x, pig.getDeltaMovement().y, forward.z);
        pig.hasImpulse = true;
    }
}

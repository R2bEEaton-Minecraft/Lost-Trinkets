package owmii.losttrinkets.core.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import owmii.losttrinkets.item.trinkets.IceShardTrinket;
import owmii.losttrinkets.item.trinkets.ThaSpiderTrinket;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void frostWalk(BlockPos pos) {
        IceShardTrinket.frostWalk(this, pos);
        super.frostWalk(pos);
    }

    @Override
    public boolean isOnLadder() {
        if (!super.isOnLadder()) {
            return ThaSpiderTrinket.doClimb(this);
        }
        return ForgeHooks.isLivingOnLadder(getBlockState(), this.world, getPosition(), this);
    }
}

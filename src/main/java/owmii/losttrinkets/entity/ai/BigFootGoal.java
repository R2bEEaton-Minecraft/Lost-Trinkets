package owmii.losttrinkets.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.item.Itms;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BigFootGoal extends Goal {
    public static final double SPEED = 1.4D;
    protected final PathNavigation navigation;
    private final PathfinderMob entity;
    @Nullable
    protected Path path;
    @Nullable
    protected Player player;

    public BigFootGoal(PathfinderMob entity) {
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.navigation = entity.getNavigation();
        this.entity = entity;
    }

    @Override
    public void start() {
        if (this.path != null) {
            this.navigation.moveTo(this.path, SPEED);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.navigation.isDone();
    }

    @Override
    public void stop() {
        this.player = null;
        this.path = null;
    }

    @Override
    public void tick() {
        this.navigation.setSpeedModifier(SPEED);
    }

    @Override
    public boolean canUse() {
        if (this.entity.isBaby()) {
            this.player = this.entity.level().getNearestPlayer(this.entity.getX(), this.entity.getY(), this.entity.getZ(), 8.0D,
                    target -> target instanceof Player player && LostTrinketsAPI.getTrinkets(player).isActive(Itms.BIG_FOOT));
            if (this.player != null) {
                Vec3 pos = DefaultRandomPos.getPosAway(this.entity, 16, 7, this.player.position());
                if (pos == null) {
                    return false;
                }
                if (this.player.distanceToSqr(pos.x, pos.y, pos.z) < this.player.distanceToSqr(this.entity)) {
                    return false;
                }
                this.path = this.navigation.createPath(BlockPos.containing(pos), 0);
                return this.path != null;
            }
        }
        return false;
    }
}

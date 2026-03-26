package owmii.losttrinkets.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public abstract class DarkEntity extends Vex {
    @Nullable
    protected UUID owner;
    @Nullable
    protected Player player;

    protected DarkEntity(EntityType<? extends Vex> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            if (this.owner == null) {
                vanish();
                return;
            }
            if (level() instanceof ServerLevel serverLevel) {
                this.player = serverLevel.getPlayerByUUID(this.owner);
            }
            if (this.player == null) {
                vanish();
                return;
            }
            if (getTarget() == null || !getTarget().isAlive()) {
                List<Mob> entities = level().getEntitiesOfClass(Mob.class, getBoundingBox().inflate(24.0D));
                boolean found = false;
                for (Mob entity : entities) {
                    if (entity.getTarget() != null && this.owner.equals(entity.getTarget().getUUID())) {
                        setTarget(entity);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    vanish();
                }
            }
        }
    }

    protected void vanish() {
        discard();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("owner")) {
            this.owner = compound.getUUID("owner");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.owner != null) {
            compound.putUUID("owner", this.owner);
        }
    }

    @Nullable
    @Override
    public Mob getOwner() {
        return null;
    }

    @Nullable
    public Player getOwnerPlayer() {
        return this.player;
    }

    public void setOwner(Player owner) {
        this.owner = owner.getUUID();
        this.player = owner;
    }
}

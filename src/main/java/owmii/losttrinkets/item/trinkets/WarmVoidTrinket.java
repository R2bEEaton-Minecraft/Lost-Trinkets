package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import owmii.losttrinkets.api.trinket.ITickableTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;

import java.util.Optional;

public class WarmVoidTrinket extends Trinket<WarmVoidTrinket> implements ITickableTrinket {
    public WarmVoidTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @Override
    public void tick(Level world, BlockPos pos, Player player) {
        if (player instanceof ServerPlayer serverPlayer && player.getY() + Math.min(0.0D, player.getDeltaMovement().y()) <= world.getMinBuildHeight() - 1) {
            if (!player.isPassenger() && !player.isVehicle()) {
                teleportToSpawnPoint(serverPlayer);
            }
        }
    }

    private static void teleportToSpawnPoint(ServerPlayer player) {
        player.stopRiding();
        SpawnPointInfo info = getSpawnPointInfo(player);
        player.setDeltaMovement(Vec3.ZERO);
        player.fallDistance = 0;
        player.teleportTo(info.spawnWorld, info.spawnPos.x, info.spawnPos.y, info.spawnPos.z, info.spawnAngle, 0.0F);
        while (!info.spawnWorld.noCollision(player) && player.getY() < info.spawnWorld.getMaxBuildHeight()) {
            player.teleportTo(info.spawnWorld, player.getX(), player.getY() + 1.0D, player.getZ(), info.spawnAngle, 0.0F);
        }
    }

    private static SpawnPointInfo getSpawnPointInfo(ServerPlayer player) {
        BlockPos spawnPosRaw = player.getRespawnPosition();
        float spawnAngle = player.getRespawnAngle();
        boolean spawnForced = player.isRespawnForced();
        ServerLevel spawnWorldRaw = player.server.getLevel(player.getRespawnDimension());
        Optional<Vec3> spawnPos;
        if (spawnWorldRaw != null && spawnPosRaw != null) {
            spawnPos = Player.findRespawnPositionAndUseSpawnBlock(spawnWorldRaw, spawnPosRaw, spawnAngle, spawnForced, true);
        } else {
            spawnPos = Optional.empty();
        }

        ServerLevel spawnWorld = spawnWorldRaw != null && spawnPos.isPresent() ? spawnWorldRaw : player.server.overworld();
        return new SpawnPointInfo(
                spawnWorld,
                spawnPos.orElseGet(() -> Vec3.atBottomCenterOf(spawnWorld.getSharedSpawnPos())),
                spawnAngle
        );
    }

    private static class SpawnPointInfo {
        public final ServerLevel spawnWorld;
        public final Vec3 spawnPos;
        public final float spawnAngle;

        public SpawnPointInfo(ServerLevel spawnWorld, Vec3 spawnPos, float spawnAngle) {
            this.spawnWorld = spawnWorld;
            this.spawnPos = spawnPos;
            this.spawnAngle = spawnAngle;
        }
    }
}

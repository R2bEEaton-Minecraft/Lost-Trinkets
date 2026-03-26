package owmii.losttrinkets.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import owmii.lib.client.util.MC;
import owmii.lib.network.IPacket;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.client.screen.Screens;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class SyncDataPacket implements IPacket<SyncDataPacket> {
    private final UUID uuid;
    private final CompoundTag nbt;

    protected SyncDataPacket(UUID uuid, CompoundTag nbt) {
        this.uuid = uuid;
        this.nbt = nbt;
    }

    public SyncDataPacket() {
        this(new UUID(0, 0), new CompoundTag());
    }

    public SyncDataPacket(Player player) {
        this(player.getUUID(), LostTrinketsAPI.getData(player).serializeNBT());
    }

    @Override
    public void encode(SyncDataPacket msg, FriendlyByteBuf buffer) {
        buffer.writeUUID(msg.uuid);
        buffer.writeNbt(msg.nbt);
    }

    @Override
    public SyncDataPacket decode(FriendlyByteBuf buffer) {
        return new SyncDataPacket(
                buffer.readUUID(),
                Objects.requireNonNull(buffer.readNbt())
        );
    }

    @Override
    public void handle(SyncDataPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            MC.world().ifPresent(world -> {
                Player player = world.getPlayerByUUID(msg.uuid);
                if (player != null) {
                    PlayerData data = LostTrinketsAPI.getData(player);
                    data.deserializeNBT(msg.nbt);
                    Screens.checkScreenRefresh();
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}

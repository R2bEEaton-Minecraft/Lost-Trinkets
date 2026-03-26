package owmii.losttrinkets.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import owmii.lib.client.util.MC;
import owmii.lib.network.IPacket;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;

import java.util.function.Supplier;

public class SyncFlyPacket implements IPacket<SyncFlyPacket> {
    private boolean fly;

    public SyncFlyPacket(boolean fly) {
        this.fly = fly;
    }

    public SyncFlyPacket() {
        this(false);
    }

    @Override
    public void encode(SyncFlyPacket msg, FriendlyByteBuf buffer) {
        buffer.writeBoolean(msg.fly);
    }

    @Override
    public SyncFlyPacket decode(FriendlyByteBuf buffer) {
        return new SyncFlyPacket(buffer.readBoolean());
    }

    @Override
    public void handle(SyncFlyPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            MC.player().ifPresent(player -> {
                PlayerData data = LostTrinketsAPI.getData(player);
                data.allowFlying = msg.fly;
                player.getAbilities().mayfly = msg.fly;
                if (!msg.fly) {
                    player.getAbilities().flying = false;
                }
                player.onUpdateAbilities();
            });
        });
        ctx.get().setPacketHandled(true);
    }
}

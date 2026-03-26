package owmii.losttrinkets.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import owmii.lib.network.IPacket;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.api.trinket.Trinkets;

import java.util.List;
import java.util.function.Supplier;

public class SetInactivePacket implements IPacket<SetInactivePacket> {
    private int trinket;

    public SetInactivePacket(int trinket) {
        this.trinket = trinket;
    }

    public SetInactivePacket() {
        this(0);
    }

    @Override
    public void encode(SetInactivePacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.trinket);
    }

    @Override
    public SetInactivePacket decode(FriendlyByteBuf buffer) {
        return new SetInactivePacket(buffer.readInt());
    }

    @Override
    public void handle(SetInactivePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            if (player != null) {
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                List<ITrinket> items = trinkets.getActiveTrinkets();
                if (!items.isEmpty()) {
                    trinkets.setInactive(items.get(msg.trinket), player);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

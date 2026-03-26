package owmii.losttrinkets.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import owmii.lib.network.IPacket;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.item.Itms;
import owmii.losttrinkets.item.trinkets.MagnetoTrinket;
import java.util.function.Supplier;

public class MagnetoPacket implements IPacket<MagnetoPacket> {
    @Override
    public void encode(MagnetoPacket msg, FriendlyByteBuf buffer) {
    }

    @Override
    public MagnetoPacket decode(FriendlyByteBuf buffer) {
        return new MagnetoPacket();
    }

    @Override
    public void handle(MagnetoPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            if (player != null && LostTrinketsAPI.getTrinkets(player).isActive(Itms.MAGNETO)) {
                MagnetoTrinket.collectNearby(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

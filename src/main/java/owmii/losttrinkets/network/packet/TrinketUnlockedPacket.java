package owmii.losttrinkets.network.packet;

import net.minecraft.world.item.Item;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import owmii.lib.client.util.MC;
import owmii.lib.network.IPacket;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.client.Sounds;
import owmii.losttrinkets.client.handler.hud.HudHandler;
import owmii.losttrinkets.client.handler.hud.Toast;

import java.util.function.Supplier;

public class TrinketUnlockedPacket implements IPacket<TrinketUnlockedPacket> {
    private String key;

    public TrinketUnlockedPacket(String key) {
        this.key = key;
    }

    public TrinketUnlockedPacket() {
        this("");
    }

    @Override
    public void encode(TrinketUnlockedPacket msg, FriendlyByteBuf buffer) {
        buffer.writeString(msg.key);
    }

    @Override
    public TrinketUnlockedPacket decode(FriendlyByteBuf buffer) {
        return new TrinketUnlockedPacket(buffer.readString(32767));
    }

    @Override
    public void handle(TrinketUnlockedPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            MC.player().ifPresent(player -> {
                Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(msg.key));
                if (item instanceof ITrinket) {
                    HudHandler.add(new Toast((ITrinket) item));
                    player.playSound(Sounds.UNLOCK, 1.0F, 1.0F);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}

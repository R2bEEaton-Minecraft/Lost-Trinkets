package owmii.losttrinkets.network.packet;

import net.minecraft.world.item.Item;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import owmii.lib.network.IPacket;
import owmii.losttrinkets.api.trinket.ITrinket;

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
        buffer.writeUtf(msg.key);
    }

    @Override
    public TrinketUnlockedPacket decode(FriendlyByteBuf buffer) {
        return new TrinketUnlockedPacket(buffer.readUtf(32767));
    }

    @Override
    public void handle(TrinketUnlockedPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientOnly.handle(msg));
        });
        ctx.get().setPacketHandled(true);
    }

    private static final class ClientOnly {
        private ClientOnly() {
        }

        private static void handle(TrinketUnlockedPacket msg) {
            owmii.lib.client.util.MC.player().ifPresent(player -> {
                Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(msg.key));
                if (item instanceof ITrinket) {
                    owmii.losttrinkets.client.handler.hud.HudHandler.add(
                            new owmii.losttrinkets.client.handler.hud.Toast((ITrinket) item)
                    );
                    player.playSound(owmii.losttrinkets.client.Sounds.UNLOCK.get(), 1.0F, 1.0F);
                }
            });
        }
    }
}

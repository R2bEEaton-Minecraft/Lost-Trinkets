package owmii.losttrinkets.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import owmii.lib.network.IPacket;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.config.Configs;

import java.util.function.Supplier;

public class UnlockSlotPacket implements IPacket<UnlockSlotPacket> {
    @Override
    public void encode(UnlockSlotPacket msg, FriendlyByteBuf buffer) {
    }

    @Override
    public UnlockSlotPacket decode(FriendlyByteBuf buffer) {
        return new UnlockSlotPacket();
    }

    @Override
    public void handle(UnlockSlotPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            if (player != null) {
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                int cost = Configs.GENERAL.calcCost(trinkets);
                if (cost >= 0) {
                    if (player.isCreative()) {
                        trinkets.unlockSlot();
                    } else if (player.experienceLevel >= cost) {
                        if (trinkets.unlockSlot()) {
                            player.addExperienceLevel(-cost);
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

package owmii.losttrinkets.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkEvent;
import owmii.lib.network.IPacket;
import owmii.lib.util.Magnet;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.item.Itms;

import java.util.List;
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
                AABB bb = new AABB(player.blockPosition()).inflate(10.0D);
                List<ItemEntity> entities = player.level().getEntitiesOfClass(ItemEntity.class, bb);
                List<ExperienceOrb> orbEntities = player.level().getEntitiesOfClass(ExperienceOrb.class, bb);
                entities.stream().filter(Magnet::canCollectManual).forEach(entity -> {
                    entity.setNoPickupDelay();
                    entity.playerTouch(player);
                });
                orbEntities.stream().filter(Magnet::canCollectManual).forEach(orb -> {
                    player.takeXpDelay = 0;
                    orb.playerTouch(player);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

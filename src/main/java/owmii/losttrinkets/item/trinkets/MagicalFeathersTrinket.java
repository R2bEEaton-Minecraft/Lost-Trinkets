package owmii.losttrinkets.item.trinkets;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.api.trinket.ITickableTrinket;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.network.packet.SyncFlyPacket;

public class MagicalFeathersTrinket extends Trinket<MagicalFeathersTrinket> implements ITickableTrinket {
    public MagicalFeathersTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @Override
    public void tick(Level world, BlockPos pos, Player player) {
        PlayerData data = LostTrinketsAPI.getData(player);
        player.getAbilities().mayfly = true;
        if (data.wasFlying) {
            player.getAbilities().flying = true;
            data.wasFlying = false;
            player.onUpdateAbilities();
        }
        if (!data.allowFlying) {
            if (!world.isClientSide && player instanceof ServerPlayer serverPlayer) {
                LostTrinkets.NET.toClient(new SyncFlyPacket(true), serverPlayer);
            }
            data.allowFlying = true;
        }
    }

    @Override
    public void onDeactivated(Level world, BlockPos pos, Player player) {
        super.onDeactivated(world, pos, player);
        PlayerData data = LostTrinketsAPI.getData(player);
        if (data.allowFlying) {
            if (!player.getAbilities().instabuild) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
            }
            player.onUpdateAbilities();
            if (!world.isClientSide && player instanceof ServerPlayer serverPlayer) {
                LostTrinkets.NET.toClient(new SyncFlyPacket(false), serverPlayer);
            }
            data.allowFlying = false;
        }
    }
}

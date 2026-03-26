package owmii.losttrinkets.impl;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.player.Player;
import owmii.losttrinkets.api.ILostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.handler.UnlockManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class LostTrinketsAPIImpl implements ILostTrinketsAPI {
    public static final Map<UUID, List<ITrinket>> UNLOCK_QUEUE = new HashMap<>();
    public static final List<UUID> WEIGHTED_UNLOCK_QUEUE = new ArrayList<>();

    @Override
    public boolean unlock(Player player, ITrinket trinket) {
        if (!player.level().isClientSide && isEnabled(trinket) && !getTrinkets(player).has(trinket)) {
            List<ITrinket> trinketList = UNLOCK_QUEUE.get(player.getUUID());
            if (trinketList != null) {
                trinketList.add(trinket);
            } else trinketList = Lists.newArrayList(trinket);
            UNLOCK_QUEUE.put(player.getUUID(), trinketList);
            return true;
        }
        return false;
    }

    @Override
    public void unlock(Player player) {
        if (!player.level().isClientSide) {
            WEIGHTED_UNLOCK_QUEUE.add(player.getUUID());
        }
    }

    @Override
    public Trinkets getTrinkets(Player player) {
        return getData(player).getTrinkets();
    }

    @Override
    public PlayerData getData(Player player) {
        return player.getCapability(PlayerData.CAP).orElseGet(PlayerData::new);
    }

    @Override
    public Set<ITrinket> getTrinkets() {
        return UnlockManager.getTrinkets();
    }

    @Override
    public Set<ITrinket> getRandomTrinkets() {
        return UnlockManager.getRandomTrinkets();
    }
}

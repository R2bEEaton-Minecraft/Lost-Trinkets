package owmii.losttrinkets.handler;

import com.google.common.collect.Sets;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.config.Configs;
import owmii.losttrinkets.network.packet.TrinketUnlockedPacket;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static owmii.lib.config.Config.MARKER;
import static owmii.losttrinkets.LostTrinkets.LOGGER;

public class UnlockManager {
    private static final Set<ITrinket> ALL_TRINKETS = ForgeRegistries.ITEMS.getValues().stream()
            .filter(item -> item instanceof ITrinket)
            .map(item -> (ITrinket) item)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    private static final Set<ITrinket> TRINKETS = Sets.newLinkedHashSet(ALL_TRINKETS);
    private static final Set<ITrinket> RANDOM_TRINKETS = Sets.newLinkedHashSet(ALL_TRINKETS);
    private static final List<WeightedTrinket> WEIGHTED_TRINKETS = new ArrayList<>();

    @Nullable
    public static ITrinket unlock(Player player, boolean checkDelay) {
        if (player instanceof ServerPlayer) {
            PlayerData data = LostTrinketsAPI.getData(player);
            if (!checkDelay || data.unlockDelay <= 0) {
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
                WEIGHTED_TRINKETS.clear();
                WEIGHTED_TRINKETS.addAll(RANDOM_TRINKETS.stream()
                        .filter(trinket -> !trinkets.has(trinket))
                        .map(WeightedTrinket::new)
                        .toList());
                if (!WEIGHTED_TRINKETS.isEmpty()) {
                    WeightedTrinket item = getRandomWeighted(player, WEIGHTED_TRINKETS);
                    if (item != null) {
                        unlock(player, item.trinket, checkDelay);
                        return item.trinket;
                    }
                }
            }
        }
        return null;
    }

    public static boolean unlock(Player player, ITrinket trinket, boolean checkDelay) {
        return unlock(player, trinket, checkDelay, true);
    }

    public static boolean unlock(Player player, ITrinket trinket, boolean checkDelay, boolean doNotification) {
        PlayerData data = LostTrinketsAPI.getData(player);
        if (!checkDelay || data.unlockDelay <= 0) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (LostTrinketsAPI.get().isEnabled(trinket) && trinkets.give(trinket)) {
                if (checkDelay) {
                    data.unlockDelay = Configs.GENERAL.unlockCooldown.get();
                }
                if (doNotification && player instanceof ServerPlayer serverPlayer) {
                    ResourceLocation key = BuiltInRegistries.ITEM.getKey(trinket.asItem());
                    LostTrinkets.NET.toClient(new TrinketUnlockedPacket(key.toString()), serverPlayer);
                    ItemStack stack = new ItemStack(trinket);
                    Component trinketName = stack.getHoverName().copy().withStyle(style ->
                            style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(stack))));
                    Component message = Component.translatable(
                            "chat.losttrinkets.unlocked.trinket",
                            player.getDisplayName(),
                            trinketName
                    ).withStyle(ChatFormatting.DARK_AQUA);
                    serverPlayer.server.getPlayerList().broadcastSystemMessage(message, false);
                }
                return true;
            }
        }
        return false;
    }

    private static WeightedTrinket getRandomWeighted(Player player, List<WeightedTrinket> entries) {
        int total = entries.stream().mapToInt(WeightedTrinket::weight).sum();
        if (total <= 0) {
            return null;
        }
        int target = player.getRandom().nextInt(total);
        int cumulative = 0;
        for (WeightedTrinket entry : entries) {
            cumulative += entry.weight();
            if (target < cumulative) {
                return entry;
            }
        }
        return entries.get(entries.size() - 1);
    }

    private record WeightedTrinket(ITrinket trinket) {
        int weight() {
            return this.trinket.getRarity().getWeight();
        }
    }

    public static void refresh() {
        Set<ResourceLocation> banned = Configs.GENERAL.blackList.get().stream()
                .map(ResourceLocation::new)
                .collect(Collectors.toCollection(Sets::newLinkedHashSet));
        Set<ResourceLocation> nonRandom = Configs.GENERAL.nonRandom.get().stream()
                .map(ResourceLocation::new)
                .collect(Collectors.toCollection(Sets::newLinkedHashSet));
        Set<ResourceLocation> seen = Sets.newLinkedHashSet();
        LOGGER.info(MARKER, "Gathering Trinkets...");
        ALL_TRINKETS.forEach(trinket -> {
            ResourceLocation rl = BuiltInRegistries.ITEM.getKey(trinket.asItem());
            seen.add(rl);
            if (banned.contains(rl)) {
                TRINKETS.remove(trinket);
                RANDOM_TRINKETS.remove(trinket);
                LOGGER.info(MARKER, "Banned: " + rl);
            } else {
                TRINKETS.add(trinket);
                if (trinket.isUnlockable() && !nonRandom.contains(rl)) {
                    RANDOM_TRINKETS.add(trinket);
                    LOGGER.debug(MARKER, "Enabled: " + rl);
                } else {
                    RANDOM_TRINKETS.remove(trinket);
                    LOGGER.info(MARKER, "Non-Random: " + rl);
                }
            }
        });
        LOGGER.info(MARKER, "All: " + ALL_TRINKETS.size());
        LOGGER.info(MARKER, "Enabled: " + TRINKETS.size() + " Disabled: " + (ALL_TRINKETS.size() - TRINKETS.size()));
        LOGGER.info(MARKER, "Random: " + RANDOM_TRINKETS.size() + " Non-Random: " + (TRINKETS.size() - RANDOM_TRINKETS.size()));
        banned.stream().filter(rl -> !seen.contains(rl))
                .forEach(rl -> LOGGER.warn(MARKER, "Unknown Banned Trinket: " + rl));
        nonRandom.stream().filter(rl -> !seen.contains(rl))
                .forEach(rl -> LOGGER.warn(MARKER, "Unknown Non-Random Trinket: " + rl));
        nonRandom.stream().filter(banned::contains)
                .forEach(rl -> LOGGER.warn(MARKER, "Redundant Non-Random Trinket (already banned): " + rl));
        if (LostTrinkets.NET != null) {
            var server = net.minecraftforge.fml.server.ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                server.execute(() -> server.getPlayerList().getPlayers()
                        .forEach(player -> LostTrinketsAPI.getTrinkets(player).removeDisabled(player)));
            }
        }
    }

    public static Set<ITrinket> getTrinkets() {
        return Collections.unmodifiableSet(TRINKETS);
    }

    public static Set<ITrinket> getRandomTrinkets() {
        return Collections.unmodifiableSet(RANDOM_TRINKETS);
    }
}

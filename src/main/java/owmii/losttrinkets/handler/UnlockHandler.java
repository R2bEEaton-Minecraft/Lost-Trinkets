package owmii.losttrinkets.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import owmii.lib.util.Ticker;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.config.Configs;
import owmii.losttrinkets.impl.LostTrinketsAPIImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class UnlockHandler {
    private static final Map<UUID, Type> MAP = new HashMap<>();
    private static final Ticker DELAY = new Ticker(10);
    private static boolean flag;

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER) {
            Player player = event.player;

            List<ITrinket> trinkets = LostTrinketsAPIImpl.UNLOCK_QUEUE.get(player.getUUID());
            if (trinkets != null) {
                trinkets.forEach(trinket -> UnlockManager.unlock(player, trinket, false));
            }
            LostTrinketsAPIImpl.UNLOCK_QUEUE.remove(player.getUUID());
            Iterator<UUID> itr = LostTrinketsAPIImpl.WEIGHTED_UNLOCK_QUEUE.iterator();
            while (itr.hasNext()) {
                if (itr.next().equals(player.getUUID())) {
                    UnlockManager.unlock(player, false);
                    itr.remove();
                }
            }
            checkUnlocks(player);
        }
    }

    private static void checkUnlocks(Player player) {
        if (Configs.GENERAL.unlockEnabled.get()) {
            UUID id = player.getUUID();
            if (DELAY.isEmpty() && MAP.containsKey(id)) {
                if (player.getRandom().nextInt(MAP.get(id).getRandom()) == 0) {
                    UnlockManager.unlock(player, true);
                }
                flag = true;
            }
            if (flag) {
                DELAY.onward();
                MAP.remove(id);
                if (DELAY.ended()) {
                    DELAY.reset();
                    flag = false;
                }
            }
        }
    }

    private static void queueUnlock(Player player, Type type) {
        if (!player.level().isClientSide && !(player instanceof FakePlayer)) {
            MAP.put(player.getUUID(), type);
        }
    }

    public static void trade(Player player) {
        if (Configs.GENERAL.unlockEnabled.get() && Configs.GENERAL.tradingUnlockEnabled.get() && !player.level().isClientSide) {
            queueUnlock(player, Type.TRADING);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void kill(LivingDeathEvent event) {
        if (Configs.GENERAL.unlockEnabled.get()) {
            DamageSource source = event.getSource();
            Entity entity = source.getEntity();
            LivingEntity target = event.getEntity();
            if (entity instanceof Player player && !player.level().isClientSide) {
                if (target.isNonBoss()) {
                    if (Configs.GENERAL.killingUnlockEnabled.get()) {
                        queueUnlock(player, Type.KILL);
                    }
                } else if (Configs.GENERAL.bossKillingUnlockEnabled.get()) {
                    queueUnlock(player, Type.BOSS_KILL);
                }
            }
        }
    }

    public static void checkBlockHarvest(Player player, Level level, BlockPos pos, BlockState state) {
        if (Configs.GENERAL.unlockEnabled.get() && !player.level().isClientSide) {
            if (Tags.Blocks.ORES.contains(state.getBlock())) {
                if (Configs.GENERAL.oresMiningUnlockEnabled.get()) {
                    queueUnlock(player, Type.ORE_MINE);
                }
            } else if (state.is(BlockTags.CROPS)) {
                if (Configs.GENERAL.farmingUnlockEnabled.get()) {
                    queueUnlock(player, Type.FARM_HARVEST);
                }
            } else if (state.is(BlockTags.LOGS)) {
                if (Configs.GENERAL.woodCuttingUnlockEnabled.get()) {
                    queueUnlock(player, Type.WOOD_CUTTING);
                }
            }
        }
    }

    @SubscribeEvent
    public static void useHoe(UseHoeEvent event) {
        if (Configs.GENERAL.unlockEnabled.get() && Configs.GENERAL.farmingUnlockEnabled.get()) {
            Player player = event.getEntity();
            if (!player.level().isClientSide) {
                queueUnlock(player, Type.FARM_HARVEST);
            }
        }
    }

    @SubscribeEvent
    public static void bonemeal(BonemealEvent event) {
        if (Configs.GENERAL.unlockEnabled.get() && Configs.GENERAL.farmingUnlockEnabled.get()) {
            Player player = event.getEntity();
            if (!player.level().isClientSide) {
                queueUnlock(player, Type.FARM_HARVEST);
            }
        }
    }

    enum Type {
        KILL, BOSS_KILL, ORE_MINE, WOOD_CUTTING, FARM_HARVEST, TRADING;

        public int getRandom() {
            if (this == KILL) {
                return Configs.GENERAL.killing.get();
            } else if (this == BOSS_KILL) {
                return Configs.GENERAL.bossKilling.get();
            } else if (this == ORE_MINE) {
                return Configs.GENERAL.oresMining.get();
            } else if (this == TRADING) {
                return Configs.GENERAL.trading.get();
            } else if (this == FARM_HARVEST) {
                return Configs.GENERAL.farming.get();
            } else if (this == WOOD_CUTTING) {
                return Configs.GENERAL.woodCutting.get();
            }
            return Integer.MAX_VALUE;
        }
    }
}

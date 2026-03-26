package owmii.losttrinkets.item.trinkets;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.Tags;
import net.minecraft.nbt.Tag;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.BlockEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

import java.util.Set;

public class OctopickTrinket extends Trinket<OctopickTrinket> {
    private static final ThreadLocal<ServerPlayer> octoMiningPlayer = new ThreadLocal<>();

    public OctopickTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onBreak(BlockEvent.BreakEvent event) {
        if (octoMiningPlayer.get() != null) return;
        try {
            Player player = event.getPlayer();
            if (player instanceof ServerPlayer serverPlayer) {
                octoMiningPlayer.set(serverPlayer);
                if (OctopickTrinket.mine(serverPlayer, serverPlayer.serverLevel(), event.getPos(), event.getState())) {
                    event.setCanceled(true);
                }
            }
        } finally {
            octoMiningPlayer.set(null);
        }
    }

    private static boolean mine(ServerPlayer player, ServerLevel world, BlockPos pos, BlockState state) {
        if (!state.requiresCorrectToolForDrops() || player.hasCorrectToolForDrops(state)) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.OCTOPICK)) {
                Set<BlockPos> toBreak = Sets.newLinkedHashSet();
                if (state.is(Tags.Blocks.ORES) || state.getBlock() == Blocks.OBSIDIAN) {
                    toBreak.add(pos);
                    for (BlockPos pos1 : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                        if (toBreak.contains(pos1)) continue;
                        BlockState state1 = world.getBlockState(pos1);
                        if (state.getBlock() == state1.getBlock()) {
                            toBreak.add(new BlockPos(pos1));
                            for (BlockPos pos2 : BlockPos.betweenClosed(pos1.offset(-1, -1, -1), pos1.offset(1, 1, 1))) {
                                if (toBreak.contains(pos2)) continue;
                                BlockState state2 = world.getBlockState(pos2);
                                if (state.getBlock() == state2.getBlock()) {
                                    toBreak.add(new BlockPos(pos2));
                                }
                            }
                        }
                    }
                }
                if (toBreak.size() > 1) {
                    toBreak.forEach(breakPos -> {
                        BlockState breakState = world.getBlockState(breakPos);
                        if (!breakState.requiresCorrectToolForDrops() || player.hasCorrectToolForDrops(breakState)) {
                            if (!breakPos.equals(pos)) {
                                int silkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem());
                                int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, player.getMainHandItem());
                                int xp = breakState.getExpDrop(world, world.random, breakPos, fortune, silkTouch);
                                if (xp > 0) {
                                    breakState.getBlock().popExperience(world, breakPos, xp);
                                }
                            }
                            if (player.gameMode.destroyBlock(breakPos)) {
                                world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, breakPos, Block.getId(breakState));
                            }
                        }
                    });
                    return true;
                }
            }
        }
        return false;
    }

    public static void collectDrops(EntityJoinLevelEvent event) {
        ServerPlayer player = octoMiningPlayer.get();
        if (player != null) {
            Entity entity = event.getEntity();
            if (entity.isAlive() && entity.level() == player.level()) {
                boolean valid = true;
                if (entity instanceof ItemEntity) {
                    ((ItemEntity) entity).setNoPickUpDelay();
                } else if (entity instanceof ExperienceOrb) {
                } else {
                    valid = false;
                }
                if (valid) {
                    Vec3 pos = player.position();
                    entity.moveTo(pos.x, pos.y, pos.z);
                    if (entity instanceof ItemEntity itemEntity) {
                        itemEntity.playerTouch(player);
                    } else if (entity instanceof ExperienceOrb orb) {
                        orb.playerTouch(player);
                    }
                    if (!entity.isAlive()) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}

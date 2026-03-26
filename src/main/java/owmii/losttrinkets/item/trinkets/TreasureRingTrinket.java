package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

import java.util.ArrayList;
import java.util.List;

public class TreasureRingTrinket extends Trinket<TreasureRingTrinket> {
    public static final List<ResourceLocation> LOOTS = new ArrayList<>();

    public TreasureRingTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    static {
        LOOTS.add(BuiltInLootTables.NETHER_BRIDGE);
        LOOTS.add(BuiltInLootTables.JUNGLE_TEMPLE);
        LOOTS.add(BuiltInLootTables.BURIED_TREASURE);
        LOOTS.add(BuiltInLootTables.END_CITY_TREASURE);
        LOOTS.add(BuiltInLootTables.ABANDONED_MINESHAFT);
        LOOTS.add(BuiltInLootTables.DESERT_PYRAMID);
        LOOTS.add(BuiltInLootTables.SIMPLE_DUNGEON);
        LOOTS.add(BuiltInLootTables.STRONGHOLD_LIBRARY);
        LOOTS.add(BuiltInLootTables.STRONGHOLD_CORRIDOR);
        LOOTS.add(BuiltInLootTables.STRONGHOLD_CROSSING);
        LOOTS.add(BuiltInLootTables.VILLAGE_WEAPONSMITH);
    }

    public static void onDrops(LivingDropsEvent event) {
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof Player player) {
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.TREASURE_RING)) {
                LivingEntity target = event.getEntity();
                if ((target instanceof EnderDragon || target instanceof WitherBoss) && player.level() instanceof ServerLevel level) {
                    LootParams.Builder builder = new LootParams.Builder(level)
                            .withParameter(LootContextParams.ORIGIN, target.position())
                            .withParameter(LootContextParams.THIS_ENTITY, player)
                            .withLuck(player.getLuck());
                    LootTable lootTable = level.getServer().getLootData().getLootTable(LOOTS.get(level.random.nextInt(LOOTS.size())));
                    List<ItemStack> stacks = lootTable.getRandomItems(builder.create(LootContextParamSets.CHEST));
                    stacks.forEach(stack -> event.getDrops().add(new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(), stack)));
                }
            }
        }
    }
}

package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.item.Itms;

import java.util.List;

@Mod.EventBusSubscriber
public class TrebleHooksTrinket extends Trinket<TrebleHooksTrinket> {
    public TrebleHooksTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    @SubscribeEvent
    public static void onFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        Level world = player.level();
        if (world instanceof ServerLevel serverLevel) {
            if (LostTrinketsAPI.getTrinkets(player).isActive(Itms.TREBLE_HOOKS)) {
                ItemStack stack = player.getMainHandItem();
                ItemStack stack1 = player.getOffhandItem();
                ItemStack rod = stack.getItem() instanceof FishingRodItem ? stack : stack1;
                for (int i = 0; i < 2; i++) {
                    FishingHook hook = event.getHookEntity();
                    Entity entity = hook.getOwner();
                    if (entity != null) {
                        LootParams.Builder builder = new LootParams.Builder(serverLevel)
                                .withParameter(LootContextParams.ORIGIN, hook.position())
                                .withParameter(LootContextParams.TOOL, rod)
                                .withParameter(LootContextParams.THIS_ENTITY, hook)
                                .withOptionalParameter(LootContextParams.KILLER_ENTITY, entity)
                                .withLuck((float) EnchantmentHelper.getFishingLuckBonus(rod) + player.getLuck());
                        MinecraftServer server = world.getServer();
                        if (server != null) {
                            LootTable loottable = server.getLootData().getLootTable(BuiltInLootTables.FISHING);
                            List<ItemStack> list = loottable.getRandomItems(builder.create(LootContextParamSets.FISHING));
                            for (ItemStack itemstack : list) {
                                ItemEntity itemEntity = new ItemEntity(world, hook.getX(), hook.getY(), hook.getZ(), itemstack);
                                double d0 = player.getX() - hook.getX();
                                double d1 = player.getY() - hook.getY();
                                double d2 = player.getZ() - hook.getZ();
                                itemEntity.setDeltaMovement(d0 * 0.1D, d1 * 0.1D + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08D, d2 * 0.1D);
                                world.addFreshEntity(itemEntity);
                            }
                        }
                    }
                }
            }
        }
    }
}

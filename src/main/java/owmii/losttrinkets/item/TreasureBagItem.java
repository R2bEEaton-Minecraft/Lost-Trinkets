package owmii.losttrinkets.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.lib.item.ItemBase;

import java.util.List;
import java.util.Objects;

public class TreasureBagItem extends ItemBase {
    public TreasureBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel) {
            LootContext.Builder builder = new LootContext.Builder(serverLevel)
                    .withParameter(LootContextParams.ORIGIN, player.position())
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .withLuck(player.getLuck());
            ResourceLocation rl = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this));
            LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(rl);
            List<ItemStack> stacks = lootTable.getRandomItems(builder.create(LootContextParamSets.GIFT));
            stacks.forEach(stack -> ItemHandlerHelper.giveItemToPlayer(player, stack.copy()));
            if (!player.isCreative()) {
                held.shrink(1);
            }
        }
        return InteractionResultHolder.consume(held);
    }
}

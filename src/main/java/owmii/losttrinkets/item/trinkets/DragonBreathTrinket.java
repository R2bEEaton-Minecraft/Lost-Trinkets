package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.level.BlockEvent;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class DragonBreathTrinket extends Trinket<DragonBreathTrinket> {
    public DragonBreathTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void onBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player.level().isClientSide) {
            return;
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem()) > 0) {
            return;
        }
        Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
        if (!trinkets.isActive(Itms.DRAGON_BREATH) || !(player.level() instanceof ServerLevel level)) {
            return;
        }

        BlockState state = event.getState();
        BlockEntity blockEntity = level.getBlockEntity(event.getPos());
        List<ItemStack> smeltedDrops = autoSmelt(Block.getDrops(state, level, event.getPos(), blockEntity, player, player.getMainHandItem()), player);
        if (smeltedDrops.isEmpty()) {
            return;
        }

        level.removeBlock(event.getPos(), false);
        smeltedDrops.forEach(stack -> Block.popResource(level, event.getPos(), stack));
        event.setCanceled(true);
    }

    public static List<ItemStack> autoSmelt(List<ItemStack> stacks, Player player) {
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem()) <= 0) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            if (trinkets.isActive(Itms.DRAGON_BREATH)) {
                List<ItemStack> drops1 = new ArrayList<>();
                List<ItemStack> stacks1 = new ArrayList<>(stacks);
                Iterator<ItemStack> itr = stacks1.iterator();
                while (itr.hasNext()) {
                    ItemStack input = itr.next();
                    Optional<SmeltingRecipe> recipe = player.level().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(input), player.level());
                    if (recipe.isPresent()) {
                        ItemStack output = recipe.get().getResultItem(player.level().registryAccess()).copy();
                        if (!output.isEmpty()) {
                            output.setCount(output.getCount() * input.getCount());
                            drops1.add(output);
                            itr.remove();
                        }
                    }
                }
                drops1.addAll(stacks1);
                return drops1;
            }
        }
        return stacks;
    }
}

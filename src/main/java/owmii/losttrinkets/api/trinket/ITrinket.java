package owmii.losttrinkets.api.trinket;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public interface ITrinket extends IForgeItem, ItemLike {
    default void addTrinketDescription(ItemStack stack, List<Component> lines) {
        lines.add(Component.translatable(Util.makeDescriptionId("info", ForgeRegistries.ITEMS.getKey(stack.getItem()))).withStyle(ChatFormatting.GRAY));
    }

    void onActivated(Level world, BlockPos pos, Player player);

    void onDeactivated(Level world, BlockPos pos, Player player);

    Rarity getRarity();

    boolean isUnlockable();

    void setUnlockable(boolean unlockable);
}

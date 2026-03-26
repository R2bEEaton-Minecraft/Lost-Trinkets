package owmii.losttrinkets.core.mixin;

import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.spongepowered.asm.mixin.Mixin;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin extends CapabilityProvider<ItemStack> implements IForgeItemStack {
    protected ItemStackMixin(Class<ItemStack> baseClass) {
        super(baseClass);
    }

    @Override
    public boolean isEnderMask(Player player, EnderMan enderman) {
        ItemStack stack = (ItemStack) (Object) this;
        boolean enderMask = stack.getItem().isEnderMask(stack, player, enderman);
        if (!enderMask) {
            Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
            return trinkets.isActive(Itms.BLANK_EYES);
        }
        return true;
    }
}

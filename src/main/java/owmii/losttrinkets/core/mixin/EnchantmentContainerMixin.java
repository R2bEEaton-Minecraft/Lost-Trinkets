package owmii.losttrinkets.core.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentContainerMixin {
    @Nullable
    private Player player;

    @Shadow
    private RandomSource random;

    @Shadow
    private DataSlot enchantmentSeed;

    @Shadow
    public int[] costs;

    @Shadow
    public int[] enchantClue;

    @Shadow
    public int[] levelClue;

    @Shadow
    public abstract List<EnchantmentInstance> getEnchantmentList(ItemStack stack, int slot, int power);

    @Shadow
    private Container enchantSlots;

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("RETURN"))
    private void enchantmentContainer(int id, Inventory inventory, ContainerLevelAccess access, CallbackInfo ci) {
        this.player = inventory.player;
    }

    @Inject(method = "slotsChanged", at = @At("HEAD"), cancellable = true)
    private void slotsChanged(Container container, CallbackInfo ci) {
        if (this.player == null || container != this.enchantSlots) {
            return;
        }
        Trinkets trinkets = LostTrinketsAPI.getTrinkets(this.player);
        if (!trinkets.isActive(Itms.BOOK_O_ENCHANTING)) {
            return;
        }
        ItemStack stack = container.getItem(0);
        if (!stack.isEmpty() && stack.isEnchantable()) {
            this.random.setSeed((long) this.enchantmentSeed.get());
            this.costs[0] = 10;
            this.costs[1] = 20;
            this.costs[2] = 30;
            this.enchantClue[0] = -1;
            this.enchantClue[1] = -1;
            this.enchantClue[2] = -1;
            this.levelClue[0] = -1;
            this.levelClue[1] = -1;
            this.levelClue[2] = -1;

            for (int i = 0; i < 3; ++i) {
                List<EnchantmentInstance> list = this.getEnchantmentList(stack, i, this.costs[i]);
                if (!list.isEmpty()) {
                    EnchantmentInstance enchantment = list.get(this.random.nextInt(list.size()));
                    this.enchantClue[i] = net.minecraft.core.registries.BuiltInRegistries.ENCHANTMENT.getId(enchantment.enchantment);
                    this.levelClue[i] = enchantment.level;
                }
            }

            ((AbstractContainerMenu) (Object) this).broadcastChanges();
        } else {
            for (int i = 0; i < 3; ++i) {
                this.costs[i] = 0;
                this.enchantClue[i] = -1;
                this.levelClue[i] = -1;
            }
        }
        ci.cancel();
    }
}

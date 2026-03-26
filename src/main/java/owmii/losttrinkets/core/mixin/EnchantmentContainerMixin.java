package owmii.losttrinkets.core.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import owmii.losttrinkets.api.LostTrinketsAPI;
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
    private Container enchantSlots;

    @Shadow
    public abstract List<EnchantmentInstance> getEnchantmentList(ItemStack stack, int slot, int power);

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("RETURN"))
    private void losttrinkets$capturePlayer(int id, Inventory inventory, net.minecraft.world.inventory.ContainerLevelAccess access, CallbackInfo ci) {
        this.player = inventory.player;
    }

    @Inject(method = "slotsChanged", at = @At("HEAD"), cancellable = true)
    private void losttrinkets$forceEnchantingTiers(Container container, CallbackInfo ci) {
        if (this.player == null || !LostTrinketsAPI.getTrinkets(this.player).isActive(Itms.BOOK_O_ENCHANTING)) {
            return;
        }

        ItemStack stack = this.enchantSlots.getItem(0);
        if (stack.isEmpty() || !stack.isEnchantable()) {
            for (int i = 0; i < 3; ++i) {
                this.costs[i] = 0;
                this.enchantClue[i] = -1;
                this.levelClue[i] = -1;
            }
            ((AbstractContainerMenu) (Object) this).broadcastChanges();
            ci.cancel();
            return;
        }

        this.random.setSeed(this.enchantmentSeed.get());
        this.costs[0] = 10;
        this.costs[1] = 20;
        this.costs[2] = 30;

        for (int i = 0; i < 3; ++i) {
            this.enchantClue[i] = -1;
            this.levelClue[i] = -1;
            List<EnchantmentInstance> list = this.getEnchantmentList(stack, i, this.costs[i]);
            if (!list.isEmpty()) {
                EnchantmentInstance enchantment = list.get(this.random.nextInt(list.size()));
                this.enchantClue[i] = BuiltInRegistries.ENCHANTMENT.getId(enchantment.enchantment);
                this.levelClue[i] = enchantment.level;
            }
        }

        ((AbstractContainerMenu) (Object) this).broadcastChanges();
        ci.cancel();
    }
}

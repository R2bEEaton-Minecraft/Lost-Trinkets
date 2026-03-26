package owmii.losttrinkets.core.mixin;

import net.minecraft.world.entity.item.EnderPearlEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderPearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.item.Itms;

@Mixin(EnderPearlItem.class)
public class EnderPearlItemMixin extends Item {
    public EnderPearlItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        player.getCooldownTracker().setCooldown(this, 20);
        if (!world.isRemote) {
            EnderPearlEntity entity = new EnderPearlEntity(world, player);
            entity.setItem(stack);
            entity.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            world.addEntity(entity);
        }
        player.addStat(Stats.ITEM_USED.get(this));

        Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
        if (!player.abilities.isCreativeMode && !trinkets.isActive(Itms.EMPTY_AMULET)) {
            stack.shrink(1);
        }

        return ActionResult.func_233538_a_(stack, world.isRemote());
    }
}

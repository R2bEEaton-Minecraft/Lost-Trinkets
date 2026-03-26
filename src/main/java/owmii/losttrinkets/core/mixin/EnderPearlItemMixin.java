package owmii.losttrinkets.core.mixin;

import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderpearlItem;
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

@Mixin(EnderpearlItem.class)
public class EnderPearlItemMixin extends Item {
    public EnderPearlItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
        player.getCooldowns().addCooldown(this, 20);
        if (!world.isClientSide) {
            ThrownEnderpearl entity = new ThrownEnderpearl(world, player);
            entity.setItem(stack);
            entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            world.addFreshEntity(entity);
        }
        player.awardStat(Stats.ITEM_USED.get(this));

        Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
        if (!player.getAbilities().instabuild && !trinkets.isActive(Itms.EMPTY_AMULET)) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
    }
}

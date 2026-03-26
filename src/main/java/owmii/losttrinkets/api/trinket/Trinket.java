package owmii.losttrinkets.api.trinket;

import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.util.MC;
import owmii.losttrinkets.api.LostTrinketsAPI;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Trinket<T extends Trinket> extends Item implements ITrinket {
    private final Map<Attribute, AttributeModifier> attributes = Maps.newHashMap();
    private final Rarity rarity;
    protected boolean unlockable = true;

    public Trinket(Rarity rarity, Properties properties) {
        super(properties.stacksTo(1));
        this.rarity = rarity;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (LostTrinketsAPI.get().unlock(player, this)) {
            ItemStack stack = player.getItemInHand(hand);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            return InteractionResultHolder.consume(stack);
        }
        return super.use(world, player, hand);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        if (LostTrinketsAPI.get().isDisabled(this)) {
            tooltip.add(Component.translatable("gui.losttrinkets.status.disabled").withStyle(ChatFormatting.DARK_RED));
        } else {
            Player player = MC.player().orElse(null);
            if (player != null && LostTrinketsAPI.getTrinkets(player).has(this)) {
                tooltip.add(Component.translatable("gui.losttrinkets.status.owned").withStyle(ChatFormatting.BLUE));
            } else if (LostTrinketsAPI.get().isNonRandom(this)) {
                tooltip.add(Component.translatable("gui.losttrinkets.status.non_random").withStyle(ChatFormatting.DARK_GRAY));
            }
        }
        addTrinketDescription(stack, tooltip);
        tooltip.add(Component.translatable("gui.losttrinkets.rarity." + getRarity().name().toLowerCase(Locale.ENGLISH)).withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack).copy().withStyle(this.getRarity().getStyle());
    }

    @Override
    public void onActivated(Level world, BlockPos pos, Player player) {
    }

    @Override
    public void onDeactivated(Level world, BlockPos pos, Player player) {
    }

    @Override
    public Rarity getRarity() {
        return this.rarity;
    }

    @Override
    public boolean isUnlockable() {
        return this.unlockable;
    }

    public Trinket noUnlock() {
        this.unlockable = false;
        return this;
    }

    @Override
    public void setUnlockable(boolean unlockable) {
        this.unlockable = unlockable;
    }

    @SuppressWarnings("unchecked")
    public T add(Attribute attribute, String uuid, double amount) {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(uuid), "Attribute", amount, AttributeModifier.Operation.ADDITION);
        getAttributes().put(attribute, attributemodifier);
        return (T) this;
    }

    public void applyAttributes(Player player) {
        for (Map.Entry<Attribute, AttributeModifier> entry : getAttributes().entrySet()) {
            AttributeInstance attribute = player.getAttribute(entry.getKey());
            if (attribute != null) {
                AttributeModifier attributeModifier = entry.getValue();
                if (!attribute.hasModifier(attributeModifier)) {
                    attribute.addPermanentModifier(attributeModifier);
                }
            }
        }
    }

    public void removeAttributes(Player player) {
        for (Map.Entry<Attribute, AttributeModifier> entry : getAttributes().entrySet()) {
            AttributeInstance attribute = player.getAttribute(entry.getKey());
            if (attribute != null) {
                attribute.removeModifier(entry.getValue());
            }
        }
    }

    public Map<Attribute, AttributeModifier> getAttributes() {
        return this.attributes;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}

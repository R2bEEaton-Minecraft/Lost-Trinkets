package owmii.losttrinkets.item.trinkets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.Rarity;
import owmii.losttrinkets.api.trinket.Trinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.client.handler.KeyHandler;
import owmii.losttrinkets.item.Itms;
import owmii.losttrinkets.network.packet.MagnetoPacket;

import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class MagnetoTrinket extends Trinket<MagnetoTrinket> {
    public MagnetoTrinket(Rarity rarity, Properties properties) {
        super(rarity, properties);
    }

    public static void trySendCollect(Player player) {
        Trinkets trinkets = LostTrinketsAPI.getTrinkets(player);
        if (trinkets.isActive(Itms.MAGNETO)) {
            LostTrinkets.NET.toServer(new MagnetoPacket());
        }
    }

    @SubscribeEvent
    public static void collectUse(PlayerInteractEvent.RightClickEmpty event) {
        if (KeyHandler.MAGNETO.isUnbound() && event.getHand() == InteractionHand.MAIN_HAND) {
            trySendCollect(event.getEntity());
        }
    }

    @Override
    public void addTrinketDescription(ItemStack stack, List<Component> lines) {
        super.addTrinketDescription(stack, lines);
        String translationKey = Util.makeDescriptionId("info", BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (KeyHandler.MAGNETO.isUnbound()) {
            lines.add(Component.translatable(translationKey + ".unbound").withStyle(ChatFormatting.GRAY));
        } else {
            lines.add(Component.translatable(translationKey + ".bound", KeyHandler.MAGNETO.getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
        }
    }
}

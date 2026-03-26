package owmii.losttrinkets.client.handler.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.StringUtils;
import owmii.lib.util.Ticker;
import owmii.losttrinkets.client.screen.Textures;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class HudHandler {
    private static final List<Toast> TOASTS = new ArrayList<>();
    private static Ticker ticker = new Ticker(60);

    @Nullable
    private static Toast toast;

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null && !TOASTS.isEmpty()) {
                TOASTS.clear();
                toast = null;
            }
            Iterator<Toast> itr = TOASTS.iterator();
            while (itr.hasNext()) {
                Toast candidate = itr.next();
                if (!candidate.getTicker().ended()) {
                    toast = candidate;
                    if (ticker.ended()) {
                        candidate.getTicker().onward();
                    }
                    ticker.add(5);
                } else {
                    ticker.back(5);
                    if (ticker.getTicks() <= 0) {
                        toast = null;
                        itr.remove();
                    }
                }
                if (toast != null) {
                    break;
                }
            }
            if (TOASTS.isEmpty()) {
                toast = null;
            }
        }
    }

    @SubscribeEvent
    public static void renderHud(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null) {
            render(event.getGuiGraphics(), mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void gui(ScreenEvent.Render.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Screen screen = event.getScreen();
        render(event.getGuiGraphics(), mc, screen.width, screen.height);
    }

    static void render(GuiGraphics guiGraphics, Minecraft mc, int width, int height) {
        if (toast != null) {
            int x = width / 2 - Textures.TOAST.getWidth() / 2;
            int y = (int) (4 - 60.0F + ticker.getTicks());
            Textures.TOAST.draw(guiGraphics, x, y);

            guiGraphics.drawString(
                    mc.font,
                    Component.translatable("gui.losttrinkets.trinket.unlocked"),
                    x + 41,
                    y + 10,
                    0xFFBA6F,
                    false
            );

            String translated = Component.translatable(toast.getTrinket().getItem().getDescriptionId()).getString();
            guiGraphics.drawString(
                    mc.font,
                    StringUtils.abbreviate(translated, 20),
                    x + 41,
                    y + 23,
                    0xF0C6E5,
                    false
            );

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x + 5.0F, y + 5.0F, 0.0F);
            guiGraphics.pose().scale(2.0F, 2.0F, 2.0F);
            guiGraphics.renderItem(new ItemStack(toast.getTrinket()), 0, 0);
            guiGraphics.pose().popPose();
        }
    }

    public static void add(Toast toast) {
        TOASTS.add(toast);
    }
}

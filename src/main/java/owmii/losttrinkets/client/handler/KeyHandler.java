package owmii.losttrinkets.client.handler;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.lib.client.util.MC;
import owmii.losttrinkets.client.screen.TrinketsScreen;
import owmii.losttrinkets.item.trinkets.MagnetoTrinket;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class KeyHandler {
    public static final String TRINKET_CATEGORY = "key.categories.losttrinkets";
    public static final KeyMapping TRINKET_GUI = new KeyMapping(
            "key.losttrinkets.trinket",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            TRINKET_CATEGORY
    );
    public static final KeyMapping MAGNETO = new KeyMapping(
            "key.losttrinkets.magneto",
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN.getValue(),
            TRINKET_CATEGORY
    );

    private KeyHandler() {
    }

    public static void register() {
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(TRINKET_GUI);
        event.register(MAGNETO);
    }

    @Mod.EventBusSubscriber(Dist.CLIENT)
    public static final class RuntimeHandler {
        private RuntimeHandler() {
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }
            while (TRINKET_GUI.consumeClick()) {
                mc.setScreen(new TrinketsScreen());
            }
            while (MAGNETO.consumeClick()) {
                MC.player().ifPresent(MagnetoTrinket::trySendCollect);
            }
        }
    }
}

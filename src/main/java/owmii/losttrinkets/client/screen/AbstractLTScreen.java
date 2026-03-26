package owmii.losttrinkets.client.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import owmii.lib.client.screen.ScreenBase;
import owmii.losttrinkets.client.handler.KeyHandler;

import javax.annotation.Nullable;

public class AbstractLTScreen extends ScreenBase {
    private boolean refresh;
    @Nullable
    private Screen toRefresh;

    protected AbstractLTScreen(Component title) {
        super(title);
    }

    @Override
    public void tick() {
        if (this.refresh && this.toRefresh != null) {
            this.minecraft.setScreen(this.toRefresh);
            this.refresh = false;
            this.toRefresh = null;
        }
    }

    public void refresh() {
        this.refresh = true;
    }

    public void setRefreshScreen(@Nullable Screen screen) {
        this.toRefresh = screen;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (KeyHandler.TRINKET_GUI.matches(keyCode, scanCode) && this.minecraft.player != null) {
            onClose();
            return true;
        }
        return false;
    }
}

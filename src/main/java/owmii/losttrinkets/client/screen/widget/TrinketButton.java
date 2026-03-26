package owmii.losttrinkets.client.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import owmii.lib.client.screen.Texture;
import owmii.losttrinkets.api.trinket.ITrinket;

import java.util.function.Consumer;

public class TrinketButton extends Button {
    private final ITrinket trinket;
    private final Texture texture;
    private final Consumer<TrinketButton> tooltipRenderer;

    public TrinketButton(int x, int y, Texture texture, ITrinket trinket, OnPress onPress, Consumer<TrinketButton> tooltipRenderer) {
        super(Button.builder(Component.empty(), onPress).bounds(x, y, texture.getWidth(), texture.getHeight()));
        this.trinket = trinket;
        this.texture = texture;
        this.tooltipRenderer = tooltipRenderer;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.texture.draw(guiGraphics, getX(), getY());
        int i = (this.texture.getWidth() - 16) / 2;
        int j = (this.texture.getHeight() - 16) / 2;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(i + getX() - 2.0D, j + getY() - 2.0D, 0.0D);
        guiGraphics.pose().scale(1.25F, 1.25F, 1.0F);
        guiGraphics.renderItem(new ItemStack(this.trinket), 0, 0);
        guiGraphics.pose().popPose();
    }

    public ITrinket getTrinket() {
        return this.trinket;
    }

    public void renderTooltip() {
        this.tooltipRenderer.accept(this);
    }
}

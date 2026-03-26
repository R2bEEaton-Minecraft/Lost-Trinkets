package owmii.losttrinkets.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.network.packet.SetInactivePacket;

import javax.annotation.Nullable;

public class TrinketOptionScreen extends AbstractLTScreen {
    private final ITrinket trinket;

    @Nullable
    protected final Screen prevScreen;

    protected TrinketOptionScreen(ITrinket trinket, @Nullable Screen prevScreen) {
        super(Component.translatable(trinket.getItem().getDescriptionId()));
        this.trinket = trinket;
        this.prevScreen = prevScreen;
    }

    @Override
    protected void init() {
        super.init();
        clearWidgets();
        int x = this.width / 2 - 60 / 2;
        int y = this.height / 3 - 20 / 2;
        if (this.mc.player != null) {
            addRenderableWidget(Button.builder(Component.literal("Remove"), button -> {
                Trinkets trinkets = LostTrinketsAPI.getTrinkets(this.mc.player);
                int i = trinkets.getActiveTrinkets().indexOf(this.trinket);
                if (i >= 0) {
                    LostTrinkets.NET.toServer(new SetInactivePacket(i));
                    trinkets.setInactive(this.trinket, this.mc.player);
                    setRefreshScreen(new TrinketsScreen());
                }
            }).bounds(x, y + 70, 60, 20).build());
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        int x = this.width / 2 - 16 / 2;
        int y = this.height / 3 - 16 / 2;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x - 16.0D, y - 16.0D, 0.0D);
        guiGraphics.pose().scale(3.0F, 3.0F, 1.0F);
        guiGraphics.renderItem(new ItemStack(this.trinket), 0, 0);
        guiGraphics.pose().popPose();

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        String name = Component.translatable(this.trinket.getItem().getDescriptionId()).getString();
        guiGraphics.drawString(this.font, name, 8 + x - this.font.width(name) / 2, y + 32, 0x999999, false);
    }

    @Override
    public void onClose() {
        if (this.prevScreen != null) {
            this.minecraft.setScreen(this.prevScreen);
        } else {
            super.onClose();
        }
    }
}

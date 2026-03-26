package owmii.losttrinkets.client.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import owmii.lib.client.screen.widget.IconButton;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.client.screen.widget.TrinketButton;
import owmii.losttrinkets.network.packet.SetActivePacket;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AvailableTrinketsScreen extends AbstractLTScreen {
    @Nullable
    protected final Screen prevScreen;
    private int columns = 14;
    private int rows = 5;
    private int btnDim = 28;
    private int headID;

    public AvailableTrinketsScreen(@Nullable Screen prevScreen, int headID) {
        super(Component.translatable("gui.losttrinkets.trinket.available"));
        this.prevScreen = prevScreen;
        this.headID = headID;
    }

    @Override
    protected void init() {
        super.init();
        clearWidgets();
        if (this.mc.player == null) {
            return;
        }
        int x = this.width / 2 - this.columns * this.btnDim / 2;
        int y = this.height / 2 - this.rows * this.btnDim / 2;
        Trinkets trinkets = LostTrinketsAPI.getTrinkets(this.mc.player);
        List<ITrinket> all = trinkets.getAvailableTrinkets();
        int total = all.size();
        int cur = 0;
        for (int row = 0; row < this.rows; ++row) {
            for (int col = 0; col < this.columns; ++col) {
                int index = col + row * this.columns + this.headID;
                if (index < total) {
                    ITrinket trinket = all.get(index);
                    addRenderableWidget(new TrinketButton(x + col * this.btnDim, y + row * this.btnDim, Textures.TRINKET_BG, trinket, button -> {
                        LostTrinkets.NET.toServer(new SetActivePacket(index));
                        trinkets.setActive(trinket, this.mc.player);
                        setRefreshScreen(new TrinketsScreen());
                    }, ignored -> {
                    }));
                    cur++;
                }
            }
        }
        int navX = x + this.columns * this.btnDim / 2 - this.btnDim / 2 - 30;
        int navY = y + 150;
        int pageSize = this.columns * this.rows;
        if (cur == pageSize && total > this.headID + pageSize) {
            addRenderableWidget(new IconButton(60 + navX, navY, Textures.TRINKET_NEXT, press ->
                    this.minecraft.setScreen(new AvailableTrinketsScreen(this.prevScreen, this.headID + pageSize)), this));
        }
        if (this.headID > 0) {
            addRenderableWidget(new IconButton(navX, navY, Textures.TRINKET_PREV, press ->
                    this.minecraft.setScreen(new AvailableTrinketsScreen(this.prevScreen, Math.max(0, this.headID - pageSize))), this));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        if (this.mc.player != null) {
            List<ITrinket> all = LostTrinketsAPI.getTrinkets(this.mc.player).getAvailableTrinkets();
            if (all.isEmpty()) {
                int x = this.width / 2;
                int y = this.height / 2;
                String name = Component.translatable("gui.losttrinkets.trinket.empty").getString();
                guiGraphics.drawString(this.font, name, x - this.font.width(name) / 2, y - 5, 0x999999, false);
            }
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        String title = getTitle().getString();
        int titleX = this.width / 2 - this.font.width(title) / 2;
        int titleY = this.height / 2 - this.rows * this.btnDim / 2 - 20;
        guiGraphics.drawString(this.font, title, titleX, titleY, 0x999999, true);

        for (var widget : this.renderables) {
            if (widget instanceof TrinketButton button && button.isHoveredOrFocused()) {
                renderTrinketTooltip(guiGraphics, button.getTrinket(), mouseX, mouseY);
            }
        }
    }

    private void renderTrinketTooltip(GuiGraphics guiGraphics, ITrinket trinket, int mouseX, int mouseY) {
        ItemStack stack = new ItemStack(trinket);
        List<Component> lines = new ArrayList<>();
        lines.add(stack.getHoverName());
        trinket.addTrinketDescription(stack, lines);
        lines.add(Component.translatable("gui.losttrinkets.rarity." + trinket.getRarity().name().toLowerCase(Locale.ENGLISH))
                .withStyle(ChatFormatting.DARK_GRAY));
        guiGraphics.renderComponentTooltip(this.font, lines, mouseX, mouseY);
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

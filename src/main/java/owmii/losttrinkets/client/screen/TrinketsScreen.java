package owmii.losttrinkets.client.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import owmii.lib.client.screen.widget.IconButton;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.api.trinket.Trinkets;
import owmii.losttrinkets.client.screen.widget.TrinketButton;
import owmii.losttrinkets.config.Configs;
import owmii.losttrinkets.network.packet.UnlockSlotPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrinketsScreen extends AbstractLTScreen {
    private int columns = 10;
    private int rows = 4;
    private int btnDim = 28;

    public TrinketsScreen() {
        super(Component.translatable("gui.losttrinkets.trinket.active"));
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
        int cost = Configs.GENERAL.calcCost(trinkets);
        List<ITrinket> all = trinkets.getActiveTrinkets();
        label:
        for (int row = 0; row < this.rows; ++row) {
            for (int col = 0; col < this.columns; ++col) {
                int index = col + row * this.columns;
                int btnX = x + col * this.btnDim;
                int btnY = y + row * this.btnDim;
                if (index < all.size()) {
                    ITrinket trinket = all.get(index);
                    addRenderableWidget(new TrinketButton(btnX, btnY, Textures.TRINKET_ACTIVE_BG, trinket, button ->
                            this.minecraft.setScreen(new TrinketOptionScreen(trinket, this)), ignored -> {
                    }));
                } else {
                    boolean locked = index + 1 > trinkets.getSlots();
                    if (locked && cost < 0) {
                        break label;
                    }
                    IconButton button = new IconButton(btnX, btnY, locked ? Textures.TRINKET_BG_LOCKED : Textures.TRINKET_BG_ADD, press -> {
                        if (locked) {
                            LostTrinkets.NET.toServer(new UnlockSlotPacket());
                            setRefreshScreen(new TrinketsScreen());
                        } else {
                            this.minecraft.setScreen(new AvailableTrinketsScreen(this, 0));
                        }
                    }, this);
                    button.setTooltip(tooltip -> {
                        if (locked) {
                            tooltip.add(Component.translatable("gui.losttrinkets.trinket.slot.locked").withStyle(ChatFormatting.DARK_PURPLE));
                            if (!this.mc.player.isCreative()) {
                                tooltip.add(Component.translatable("gui.losttrinkets.trinket.slot.cost", cost).withStyle(ChatFormatting.DARK_GRAY));
                            }
                            tooltip.add(Component.empty());
                            tooltip.add(Component.translatable("gui.losttrinkets.trinket.slot.click.unlock").withStyle(ChatFormatting.GRAY));
                        } else {
                            tooltip.add(Component.translatable("gui.losttrinkets.trinket.slot.click.add").withStyle(ChatFormatting.GRAY));
                        }
                    });
                    addRenderableWidget(button);
                    if (locked) {
                        break label;
                    }
                }
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        String title = getTitle().getString();
        int x = this.width / 2 - this.font.width(title) / 2;
        int y = this.height / 2 - this.rows * this.btnDim / 2 - 20;
        guiGraphics.drawString(this.font, title, x, y, 0x999999, true);

        for (var widget : this.renderables) {
            if (widget instanceof TrinketButton button && button.isHoveredOrFocused()) {
                renderTrinketTooltip(guiGraphics, button.getTrinket(), mouseX, mouseY);
            } else if (widget instanceof IconButton button && button.isHoveredOrFocused()) {
                button.renderToolTip(guiGraphics, mouseX, mouseY);
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
}

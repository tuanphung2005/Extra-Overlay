package org.moss.extraoverlay.client.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;

public interface IOverlay {
    String getName();
    int getX();
    int getY();

    void setPosition(int x, int y);
    void render(DrawContext context, MinecraftClient client);
    void renderPreview(DrawContext context, TextRenderer textRenderer, int previewX, int previewY);

    boolean isSelected();
    void setSelected(boolean selected);

    int getWidth();
    int getHeight();

}
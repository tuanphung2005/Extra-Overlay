package org.moss.extraoverlay.client.overlay;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;

public class CoordinateOverlay {
    private static int x = 5;
    private static int y = 5;

    public static int getX() { return x; }
    public static int getY() { return y; }
    
    public static void setPosition(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public static void render(DrawContext context, MinecraftClient client) {
        if (client.player == null) return;

        String[] coordinates = {
            String.format("X: %.2f", client.player.getX()),
            String.format("Y: %.2f", client.player.getY()),
            String.format("Z: %.2f", client.player.getZ())
        };

        renderCoordinates(context, client.textRenderer, coordinates, x, y);
    }

    public static void renderPreview(DrawContext context, TextRenderer textRenderer, int previewX, int previewY) {
        String[] coordinates = {
            "X: 123.45",
            "Y: 69.420",
            "Z: -420.69"
        };

        renderCoordinates(context, textRenderer, coordinates, previewX, previewY);
    }

    private static void renderCoordinates(DrawContext context, TextRenderer textRenderer, String[] coordinates, int posX, int posY) {
        int lineHeight = textRenderer.fontHeight + 2;
        int maxWidth = 0;
        
        for (String coord : coordinates) {
            maxWidth = Math.max(maxWidth, textRenderer.getWidth(coord));
        }
        maxWidth += 4;

        context.fill(
            posX - 2,
            posY - 2,
            posX + maxWidth,
            posY + (coordinates.length * lineHeight),
            0x80000000
        );

        for (int i = 0; i < coordinates.length; i++) {
            context.drawTextWithShadow(
                textRenderer,
                coordinates[i],
                posX,
                posY + (i * lineHeight),
                0xFFFFFF
            );
        }
    }
}
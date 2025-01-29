package org.moss.extraoverlay.client.overlay;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;

public class CoordinateOverlay implements IOverlay {
    private static int x = 5;
    private static int y = 5;
    private boolean selected = false;

    @Override
    public String getName() {
        return "Coordinates";
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setPosition(int newX, int newY) {
        x = newX;
        y = newY;
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        if (client.player == null) return;

        String[] coordinates = {
            String.format("X: %.2f", client.player.getX()),
            String.format("Y: %.2f", client.player.getY()),
            String.format("Z: %.2f", client.player.getZ())
        };

        renderCoordinates(context, client.textRenderer, coordinates, x, y);
    }

    @Override
    public void renderPreview(DrawContext context, TextRenderer textRenderer, int previewX, int previewY) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
    
        String[] coordinates = {
            String.format("X: %.2f", client.player.getX()),
            String.format("Y: %.2f", client.player.getY()),
            String.format("Z: %.2f", client.player.getZ())
        };
    
        renderCoordinates(context, textRenderer, coordinates, previewX, previewY);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int getWidth() {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        
        String[] coords = new String[] {
            "X: %.2f" + (int)client.player.getX(),
            "Y: %.2f" + (int)client.player.getY(), 
            "Z: %.2f" + (int)client.player.getZ()
        };
        
        int maxWidth = 0;
        for (String coord : coords) {
            maxWidth = Math.max(maxWidth, textRenderer.getWidth(coord));
        }
        
        return maxWidth + 4;
    }

    @Override 
    public int getHeight() {
        int lineHeight = MinecraftClient.getInstance().textRenderer.fontHeight + 2;
        return (lineHeight * 3);
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

        // Draw text
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
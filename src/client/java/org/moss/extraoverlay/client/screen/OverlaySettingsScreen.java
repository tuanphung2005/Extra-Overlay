package org.moss.extraoverlay.client.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;

import org.moss.extraoverlay.client.overlay.IOverlay;
import org.moss.extraoverlay.client.overlay.OverlayManager;

public class OverlaySettingsScreen extends Screen {
    private final Screen parent;
    private boolean isDragging = false;
    private IOverlay draggingOverlay = null;
    private int dragOffsetX;
    private int dragOffsetY;

    public OverlaySettingsScreen(Screen parent) {
        super(Text.literal("Edit Overlays"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // Save/Done
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Done"),
            button -> client.setScreen(this.parent))
            .dimensions(this.width / 2 - 100, this.height - 28, 200, 20)
            .build()
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        for (IOverlay overlay : OverlayManager.getOverlays()) {
            if (isMouseOver(mouseX, mouseY, overlay)) {
                isDragging = true;
                draggingOverlay = overlay;
                dragOffsetX = (int)mouseX - overlay.getX();
                dragOffsetY = (int)mouseY - overlay.getY();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isMouseOver(double mouseX, double mouseY, IOverlay overlay) {
        int x = overlay.getX();
        int y = overlay.getY();

        return mouseX >= x && mouseX <= x + 200 &&
               mouseY >= y && mouseY <= y + 50;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isDragging = false;
        draggingOverlay = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging && draggingOverlay != null) {
            int newX = (int)mouseX - dragOffsetX;
            int newY = (int)mouseY - dragOffsetY;
            draggingOverlay.setPosition(newX, newY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw a semi-transparent dark background
        context.fill(0, 0, this.width, this.height, 0x88000000);

        // Instructions
        context.drawText(client.textRenderer, 
            "Click and drag overlays to reposition them", 
            10, 10, 0xFFFFFF, true);

        // Only render preview overlays
        for (IOverlay overlay : OverlayManager.getOverlays()) {
            // Only render preview, main overlays will be hidden while in settings
            overlay.renderPreview(context, client.textRenderer, overlay.getX(), overlay.getY());
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
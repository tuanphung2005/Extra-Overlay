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
        // Deselect
        for (IOverlay overlay : OverlayManager.getOverlays()) {
            overlay.setSelected(false);
        }

        // new selection
        for (IOverlay overlay : OverlayManager.getOverlays()) {
            if (isMouseOver(mouseX, mouseY, overlay)) {
                isDragging = true;
                draggingOverlay = overlay;
                dragOffsetX = (int)mouseX - overlay.getX();
                dragOffsetY = (int)mouseY - overlay.getY();
                overlay.setSelected(true);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isMouseOver(double mouseX, double mouseY, IOverlay overlay) {
        int x = overlay.getX();
        int y = overlay.getY();
        int width = overlay.getWidth();
        int height = overlay.getHeight();

        return mouseX >= x && mouseX <= x + width &&
               mouseY >= y && mouseY <= y + height;
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

        context.fill(0, 0, this.width, this.height, 0x88000000);

        context.drawText(client.textRenderer, 
            "Click and drag overlays to reposition them", 
            10, 10, 0xFFFFFF, true);

        for (IOverlay overlay : OverlayManager.getOverlays()) {
            overlay.renderPreview(context, client.textRenderer, overlay.getX(), overlay.getY());
        }

        // overlays
        for (IOverlay overlay : OverlayManager.getOverlays()) {
            if (overlay.isSelected()) {
                int x = overlay.getX();
                int y = overlay.getY();
                int w = overlay.getWidth();
                int h = overlay.getHeight();
                
                // border
                context.fill(x - 1, y - 1, x + w + 1, y, 0xFFFFFFFF); // Top
                context.fill(x - 1, y, x, y + h, 0xFFFFFFFF); // Left
                context.fill(x + w, y, x + w + 1, y + h, 0xFFFFFFFF); // Right
                context.fill(x - 1, y + h, x + w + 1, y + h + 1, 0xFFFFFFFF); // Bottom
            }
            
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
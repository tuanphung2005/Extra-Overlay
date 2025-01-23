package org.moss.extraoverlay.client.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import org.moss.extraoverlay.client.overlay.CoordinateOverlay;

public class CoordOverlayScreen extends Screen {
    private final Screen parent;
    private int previewX;
    private int previewY;
    private boolean isDragging = false;
    private int dragOffsetX;
    private int dragOffsetY;

    public CoordOverlayScreen(Screen parent) {
        super(Text.literal("Coordinate Overlay Settings"));
        this.parent = parent;
        this.previewX = CoordinateOverlay.getX();
        this.previewY = CoordinateOverlay.getY();
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Save"),
            button -> {
                CoordinateOverlay.setPosition(previewX, previewY);
                client.setScreen(this.parent);
            })
            .dimensions(this.width / 2 - 100, this.height - 28, 200, 20)
            .build()
        );
    }

    // Mouse handling
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= previewX && mouseX <= previewX + 200 &&
            mouseY >= previewY && mouseY <= previewY + 50) {
            isDragging = true;
            dragOffsetX = (int)mouseX - previewX;
            dragOffsetY = (int)mouseY - previewY;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isDragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging) {
            // maintain relative position
            previewX = (int)mouseX - dragOffsetX;
            previewY = (int)mouseY - dragOffsetY;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0x88000000);
        context.drawText(client.textRenderer, "Drag the preview to position the overlay", 10, 10, 0xFFFFFF, true);
        
        CoordinateOverlay.renderPreview(context, client.textRenderer, previewX, previewY);
        super.render(context, mouseX, mouseY, delta);
    }
}
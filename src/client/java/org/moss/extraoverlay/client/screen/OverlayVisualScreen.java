package org.moss.extraoverlay.client.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import org.moss.extraoverlay.client.overlay.IOverlay;
import org.moss.extraoverlay.client.overlay.OverlayManager;

public class OverlayVisualScreen extends Screen {
    private final GlobalSettingsScreen parent;
    private IOverlay selectedOverlay = null;
    private static final int SIDEBAR_WIDTH = 120;
    private static final int PADDING = 10;
    private static final int TITLE_HEIGHT = 30; // Add title height constant
    
    public OverlayVisualScreen(GlobalSettingsScreen parent) {
        super(Text.literal("Visual Settings"));
        this.parent = parent;
    }
    
    @Override
    protected void init() {
        // Sidebar - Overlay Selection Buttons
        int buttonY = PADDING;
        for (IOverlay overlay : OverlayManager.getOverlays()) {
            addDrawableChild(ButtonWidget.builder(
                Text.literal(overlay.getName()),
                button -> {
                    selectedOverlay = overlay;
                    initSettingsButtons(); // Refresh settings area
                })
                .dimensions(PADDING, buttonY, SIDEBAR_WIDTH - (PADDING * 2), 20)
                .build()
            );
            buttonY += 24;
        }

        // Back Button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Back"),
            button -> client.setScreen(this.parent))
            .dimensions(this.width / 2 - 100, this.height - 28, 200, 20)
            .build()
        );

        // Initialize settings for first overlay
        if (selectedOverlay == null && !OverlayManager.getOverlays().isEmpty()) {
            selectedOverlay = OverlayManager.getOverlays().get(0);
        }
        initSettingsButtons();
    }

    private void initSettingsButtons() {
        clearSettingsButtons();
        
        if (selectedOverlay == null) return;

        // Add settings buttons for selected overlay
        int settingsX = SIDEBAR_WIDTH + PADDING;
        int settingsY = PADDING + TITLE_HEIGHT; // Move buttons below title
        int settingsWidth = 200;

        // Background Color Button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Background Color"),
            button -> {/* TODO: Implement color picker */})
            .dimensions(settingsX, settingsY, settingsWidth, 20)
            .build()
        );

        // Text Color Button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Text Color"),
            button -> {/* TODO: Implement color picker */})
            .dimensions(settingsX, settingsY + 24, settingsWidth, 20)
            .build()
        );

        // Background Opacity Slider
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Background Opacity"),
            button -> {/* TODO: Implement opacity slider */})
            .dimensions(settingsX, settingsY + 48, settingsWidth, 20)
            .build()
        );

        // Back Button - Move to bottom of screen
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Back"),
            button -> {
                // Fix back button functionality
                if (this.client != null) {
                    this.client.setScreen(this.parent);
                }
            })
            .dimensions(this.width / 2 - 100, this.height - 28, 200, 20)
            .build()
        );
    }

    private void clearSettingsButtons() {
        // Remove all buttons except sidebar and back button
        this.children().removeIf(child -> {
            if (child instanceof ButtonWidget button) {
                int x = button.getX();
                return x > SIDEBAR_WIDTH; // Remove if in settings area
            }
            return false;
        });
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw background
        this.renderBackground(context, mouseX, mouseY, delta);

        // Draw sidebar background
        context.fill(0, 0, SIDEBAR_WIDTH, this.height, 0x88000000);
        
        // Draw separator line
        context.fill(SIDEBAR_WIDTH, 0, SIDEBAR_WIDTH + 1, this.height, 0xFFFFFFFF);

        // Draw title
        context.drawTextWithShadow(
            this.textRenderer,
            this.title,
            (this.width - this.textRenderer.getWidth(this.title.getString())) / 2,
            10,
            0xFFFFFF
        );

        // Draw selected overlay name with proper spacing
        if (selectedOverlay != null) {
            context.drawTextWithShadow(
                this.textRenderer,
                "Settings for " + selectedOverlay.getName(),
                SIDEBAR_WIDTH + PADDING,
                PADDING + 10, // Add some padding from top
                0xFFFFFF
            );
        }

        super.render(context, mouseX, mouseY, delta);
    }
}
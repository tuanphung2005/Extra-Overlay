package org.moss.extraoverlay.client.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import org.moss.extraoverlay.client.overlay.IOverlay;
import org.moss.extraoverlay.client.overlay.OverlayManager;
import org.moss.extraoverlay.client.overlay.OverlaySetting;

import java.util.List;

public class OverlayVisualScreen extends Screen {
    private final GlobalSettingsScreen parent;
    private IOverlay selectedOverlay = null;
    private static final int SIDEBAR_WIDTH = 120;
    private static final int PADDING = 10;
    private static final int TITLE_HEIGHT = 30;
    
    public OverlayVisualScreen(GlobalSettingsScreen parent) {
        super(Text.literal("Visual Settings"));
        this.parent = parent;
    }
    
    @Override
    protected void init() {
        // Sidebar
        int buttonY = PADDING;
        for (IOverlay overlay : OverlayManager.getOverlays()) {
            addDrawableChild(ButtonWidget.builder(
                Text.literal(overlay.getName()),
                button -> {
                    selectedOverlay = overlay;
                    clearAndInitSettings();
                })
                .dimensions(PADDING, buttonY, SIDEBAR_WIDTH - (PADDING * 2), 20)
                .build()
            );
            buttonY += 24;
        }

        // Back
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Back"),
            button -> client.setScreen(this.parent))
            .dimensions(this.width / 2 - 100, this.height - 28, 200, 20)
            .build()
        );

        if (selectedOverlay == null && !OverlayManager.getOverlays().isEmpty()) {
            selectedOverlay = OverlayManager.getOverlays().get(0);
        }
        initSettingsButtons();
    }

    protected void clearAndInitSettings() {
        clearChildren();
        init();
    }

    private void initSettingsButtons() {
        if (selectedOverlay == null) return;

        int settingsX = SIDEBAR_WIDTH + PADDING;
        int settingsY = PADDING + TITLE_HEIGHT;
        int settingsWidth = 200;
        int spacing = 24;

        List<OverlaySetting<?>> settings = selectedOverlay.getSettings();
        for (int i = 0; i < settings.size(); i++) {
            OverlaySetting<?> setting = settings.get(i);
            addSettingButton(setting, settingsX, settingsY + (i * spacing), settingsWidth);
        }
    }

    private void addSettingButton(OverlaySetting<?> setting, int x, int y, int width) {
        switch(setting.getType()) {
            case TOGGLE:
                addToggleButton(setting, x, y, width);
                break;
            case COLOR:
                addColorButton(setting, x, y, width);
                break;
            case SLIDER:
                addSliderButton(setting, x, y, width);
                break;
        }
    }

    private void clearSettingsButtons() {

        this.children().removeIf(child -> {
            if (child instanceof ButtonWidget button) {

                int x = button.getX();
                boolean isSidebarButton = x <= SIDEBAR_WIDTH;
                boolean isBackButton = button.getY() == this.height - 28;
                return !isSidebarButton && !isBackButton;
            }
            return false;
        });
    }

    private void addToggleButton(OverlaySetting<?> setting, int x, int y, int width) {

        @SuppressWarnings("unchecked")
        OverlaySetting<Boolean> toggleSetting = (OverlaySetting<Boolean>) setting;
        
        addDrawableChild(ButtonWidget.builder(
            Text.literal(setting.getName() + ": " + (toggleSetting.getValue() ? "ON" : "OFF")),
            button -> {

                boolean newValue = !toggleSetting.getValue();
                toggleSetting.setValue(newValue);

                button.setMessage(Text.literal(setting.getName() + ": " + (newValue ? "ON" : "OFF")));

                selectedOverlay.updateSetting(setting.getId(), newValue);
            })
            .dimensions(x, y, width, 20)
            .build()
        );
    }

    private void addColorButton(OverlaySetting<?> setting, int x, int y, int width) {

        @SuppressWarnings("unchecked")
        OverlaySetting<Integer> colorSetting = (OverlaySetting<Integer>) setting;

        addDrawableChild(ButtonWidget.builder(
            Text.literal(setting.getName()),
            button -> {
                // TODO: Add color picker implementation
                int[] presetColors = {
                    0x80000000, // Default transparent black
                    0x80FF0000, // Transparent red
                    0x8000FF00, // Transparent green
                    0x800000FF, // Transparent blue
                    0x80FFFFFF  // Transparent white
                };
                
                int currentColor = colorSetting.getValue();
                int currentIndex = -1;
                
                // Find current color index, get next color TODO: remove this once implement color picker
                for (int i = 0; i < presetColors.length; i++) {
                    if (presetColors[i] == currentColor) {
                        currentIndex = i;
                        break;
                    }
                }
                
                int nextIndex = (currentIndex + 1) % presetColors.length;
                int newColor = presetColors[nextIndex];
                
                colorSetting.setValue(newColor);
                selectedOverlay.updateSetting(setting.getId(), newColor);
            })
            .dimensions(x, y, width - 30, 20)
            .build()
        );
        
        addDrawableChild(ButtonWidget.builder(
            Text.literal(""),
            button -> {})
            .dimensions(x + width - 25, y, 20, 20)
            .build()
        );
    }

    private void addSliderButton(OverlaySetting<?> setting, int x, int y, int width) {

        @SuppressWarnings("unchecked")
        OverlaySetting<Double> sliderSetting = (OverlaySetting<Double>) setting;
        

        double value = sliderSetting.getValue();

        double min = 0.0;
        double max = 1.0;
        
        addDrawableChild(ButtonWidget.builder(
            Text.literal(setting.getName() + ": " + String.format("%.2f", value)),
            button -> {

                double newValue = value + 0.1;
                if (newValue > max) {
                    newValue = min;
                }
                
                sliderSetting.setValue(newValue);
                
                button.setMessage(Text.literal(setting.getName() + ": " + String.format("%.2f", newValue)));
                
                selectedOverlay.updateSetting(setting.getId(), newValue);
            })
            .dimensions(x, y, width, 20)
            .build()
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        context.fill(0, 0, SIDEBAR_WIDTH, this.height, 0x88000000);

        context.fill(SIDEBAR_WIDTH, 0, SIDEBAR_WIDTH + 1, this.height, 0xFFFFFFFF);

        context.drawTextWithShadow(
            this.textRenderer,
            this.title,
            (this.width - this.textRenderer.getWidth(this.title.getString())) / 2,
            10,
            0xFFFFFF
        );

        super.render(context, mouseX, mouseY, delta);
        
        if (selectedOverlay != null) {
            for (OverlaySetting<?> setting : selectedOverlay.getSettings()) {
                if (setting.getType() == OverlaySetting.SettingType.COLOR) {
                    this.children().stream()
                        .filter(child -> child instanceof ButtonWidget)
                        .map(child -> (ButtonWidget)child)
                        .filter(button -> button.getX() == SIDEBAR_WIDTH + PADDING + 200 - 25)
                        .findFirst()
                        .ifPresent(button -> {
                            @SuppressWarnings("unchecked")
                            OverlaySetting<Integer> colorSetting = (OverlaySetting<Integer>) setting;
                            int color = colorSetting.getValue();
                            context.fill(
                                button.getX() + 2,
                                button.getY() + 2,
                                button.getX() + 18,
                                button.getY() + 18,
                                color
                            );
                        });
                }
            }
        }
    }
}
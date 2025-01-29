package org.moss.extraoverlay.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;

import org.moss.extraoverlay.client.screen.GlobalSettingsScreen;

public class GlobalSettingsScreen extends Screen {
    private final Screen parent;
    private Screen currentScreen;
    private final MinecraftClient client;
    
    public GlobalSettingsScreen(Screen parent) {
        super(Text.literal("Extra Overlay Settings"));
        this.parent = parent;
        this.currentScreen = null;
        this.client = MinecraftClient.getInstance();
    }
    
    @Override
    protected void init() {
        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 24;
        int startY = this.height / 4;
        
        // Position Settings Button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Position Overlays"),
            button -> client.setScreen(new OverlayPositionScreen(this))) // Changed to directly set screen
            .dimensions(this.width / 2 - buttonWidth / 2, startY, buttonWidth, buttonHeight)
            .build()
        );
        
        // Visual Settings Button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Visual Settings"),
            button -> client.setScreen(new OverlayVisualScreen(this))) // Changed to directly set screen
            .dimensions(this.width / 2 - buttonWidth / 2, startY + spacing, buttonWidth, buttonHeight)
            .build()
        );
        
        // Done Button
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Done"),
            button -> this.client.setScreen(this.parent))
            .dimensions(this.width / 2 - buttonWidth / 2, this.height - 28, buttonWidth, buttonHeight)
            .build()
        );
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (currentScreen != null) {
            currentScreen.render(context, mouseX, mouseY, delta);
            return;
        }
        
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(
            this.textRenderer,
            this.title,
            (this.width - this.textRenderer.getWidth(this.title.getString())) / 2,
            10,
            0xFFFFFF
        );
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (currentScreen != null) {
            return currentScreen.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
}
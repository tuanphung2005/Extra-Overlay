package org.moss.extraoverlay.mixin.client;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.moss.extraoverlay.client.screen.OverlaySettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addCustomButton(CallbackInfo ci) {

        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Edit Overlays"),
            button -> this.client.setScreen(new OverlaySettingsScreen(this)))
            .dimensions(this.width / 2 - 100, this.height / 4 + 144, 200, 20)
            .build()
        );
    }
}
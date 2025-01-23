package org.moss.extraoverlay.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.moss.extraoverlay.client.overlay.CoordinateOverlay;
import org.moss.extraoverlay.client.overlay.ArmorOverlay;
import net.minecraft.client.MinecraftClient;
import org.moss.extraoverlay.client.overlay.IOverlay;
import org.moss.extraoverlay.client.overlay.OverlayManager;

public class ExtraoverlayClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // overlays
        OverlayManager.registerOverlay(new CoordinateOverlay());
        OverlayManager.registerOverlay(new ArmorOverlay());
        
        // HUD renderer
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            for (IOverlay overlay : OverlayManager.getOverlays()) {
                overlay.render(drawContext, client);
            }
        });
    }
}
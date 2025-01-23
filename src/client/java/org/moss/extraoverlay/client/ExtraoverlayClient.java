package org.moss.extraoverlay.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.moss.extraoverlay.client.overlay.CoordinateOverlay;
import net.minecraft.client.MinecraftClient;

public class ExtraoverlayClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // coord overlay
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            CoordinateOverlay.render(drawContext, MinecraftClient.getInstance());
        });
    }
}
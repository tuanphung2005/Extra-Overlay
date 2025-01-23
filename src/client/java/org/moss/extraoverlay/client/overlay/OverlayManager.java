package org.moss.extraoverlay.client.overlay;

import java.util.ArrayList;
import java.util.List;

public class OverlayManager {
    private static final List<IOverlay> overlays = new ArrayList<>();
    
    public static void registerOverlay(IOverlay overlay) {
        overlays.add(overlay);
    }
    
    public static List<IOverlay> getOverlays() {
        return overlays;
    }
    
    public static IOverlay getOverlay(int index) {
        return overlays.get(index);
    }
}
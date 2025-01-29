package org.moss.extraoverlay.client.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ArmorOverlay implements IOverlay {
    private static int x = 5;
    private static int y = 80;
    private boolean selected = false;

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String getName() {
        return "Armor Status";
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setPosition(int newX, int newY) {
        x = newX;
        y = newY;
    }

    @Override
    public int getWidth() {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        
        // Account for both item icon (16px) and text width
        int iconWidth = 16;
        int spacing = 4;
        
        // Get max text width
        int maxTextWidth = textRenderer.getWidth("100%"); // Max possible text width
        
        return iconWidth + spacing + maxTextWidth;
    }

    @Override
    public int getHeight() {
        int lineHeight = 20; // Same as used in renderArmorStatus
        return lineHeight * 4; // 4 armor pieces
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        if (client.player == null) return;

        ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chestplate = client.player.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack leggings = client.player.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack boots = client.player.getEquippedStack(EquipmentSlot.FEET);
        
        ItemStack[] armor = {helmet, chestplate, leggings, boots};
        
        List<ItemStack> equippedArmor = new ArrayList<>();
        List<String> durabilities = new ArrayList<>();
        
        for (ItemStack piece : armor) {
            if (!piece.isEmpty()) {
                equippedArmor.add(piece);
                durabilities.add(String.format("%d%%", getArmorDurability(piece)));
            }
        }
        
        if (!equippedArmor.isEmpty()) {
            renderArmorStatus(
                context, 
                client.textRenderer, 
                equippedArmor.toArray(new ItemStack[0]),
                durabilities.toArray(new String[0]),
                x, 
                y
            );
        }
    }

    @Override
    public void renderPreview(DrawContext context, TextRenderer textRenderer, int previewX, int previewY) {
        ItemStack[] dummyArmor = {
            new ItemStack(Items.DIAMOND_HELMET),
            new ItemStack(Items.DIAMOND_CHESTPLATE),
            new ItemStack(Items.DIAMOND_LEGGINGS),
            new ItemStack(Items.DIAMOND_BOOTS)
        };
        String[] dummyDurabilities = {"100%", "85%", "92%", "78%"};
        
        renderArmorStatus(context, textRenderer, dummyArmor, dummyDurabilities, previewX, previewY);
    }

    private static int getArmorDurability(ItemStack stack) {
        if (stack.isEmpty() || !stack.isDamageable()) return 0;
        return (int)(((stack.getMaxDamage() - stack.getDamage()) * 100.0f) / stack.getMaxDamage());
    }

    private static void renderArmorStatus(DrawContext context, TextRenderer textRenderer, ItemStack[] items, String[] durabilities, int posX, int posY) {
        int lineHeight = 20;
        
        for (int i = 0; i < items.length; i++) {
            // Draw item
            context.drawItem(items[i], posX, posY + (i * lineHeight));
            
            // Draw durability
            int color = getColorForDurability(durabilities[i]);
            context.drawTextWithShadow(
                textRenderer,
                durabilities[i],
                posX + 20,
                posY + (i * lineHeight) + 5,
                color
            );
        }
    }

    private static int getColorForDurability(String text) {
        try {
            int durability = Integer.parseInt(text.replaceAll("[^0-9]", ""));
            if (durability <= 25) return 0xFFFF5555;
            if (durability <= 50) return 0xFFFFAA00;
            return 0xFF55FF55;
        } catch (NumberFormatException e) {
            return 0xFFFFFFFF;
        }
    }
}
package org.moss.extraoverlay.client.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;

public class ArmorOverlay implements IOverlay {
    private static int x = 5;
    private static int y = 80;

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
    public void render(DrawContext context, MinecraftClient client) {
        if (client.player == null) return;

        ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chestplate = client.player.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack leggings = client.player.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack boots = client.player.getEquippedStack(EquipmentSlot.FEET);
        
        ItemStack[] armor = {helmet, chestplate, leggings, boots};
        
        boolean hasArmor = false;
        for (ItemStack item : armor) {
            if (!item.isEmpty()) {
                hasArmor = true;
                break;
            }
        }
        
        if (hasArmor) {
            String[] durabilities = new String[4];
            for (int i = 0; i < armor.length; i++) {
                durabilities[i] = String.format("%d%%", getArmorDurability(armor[i]));
            }
            renderArmorStatus(context, client.textRenderer, armor, durabilities, x, y);
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
        int maxWidth = 0;
        
        for (String text : durabilities) {
            maxWidth = Math.max(maxWidth, textRenderer.getWidth(text));
        }
        maxWidth += 24;

        // background
        context.fill(
            posX - 2,
            posY - 2,
            posX + maxWidth,
            posY + (items.length * lineHeight),
            0x80000000
        );

        for (int i = 0; i < items.length; i++) {
            if (!items[i].isEmpty()) {
                // item
                context.drawItem(items[i], posX, posY + (i * lineHeight));
                
                // durability text
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
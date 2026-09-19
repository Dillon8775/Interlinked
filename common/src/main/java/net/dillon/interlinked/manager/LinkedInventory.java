package net.dillon.interlinked.manager;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class LinkedInventory {
    public static final int SIZE = 43;
    private final ItemStack[] items = new ItemStack[SIZE];

    public LinkedInventory(Inventory source) {
        for (int i = 0; i < SIZE; i++) {
            items[i] = source.getItem(i).copy();
        }
    }

    public ItemStack getItem(int slot) {
        if (slot < 0 || slot >= SIZE) {
            return ItemStack.EMPTY;
        }

        return items[slot];
    }

    public void setItem(int slot, ItemStack stack) {
        if (slot < 0 || slot >= SIZE) {
            return;
        }

        items[slot] = stack.copy();
    }

    public void copyFrom(Inventory inventory) {
        for (int i = 0; i < SIZE; i++) {
            items[i] = inventory.getItem(i).copy();
        }
    }

    public void copyTo(Inventory inventory) {
        for (int i = 0; i < SIZE; i++) {
            inventory.setItem(i, items[i].copy());
        }
    }
}
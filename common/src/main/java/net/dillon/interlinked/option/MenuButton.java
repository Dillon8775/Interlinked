package net.dillon.interlinked.option;

import net.minecraft.util.StringRepresentable;

public enum MenuButton implements StringRepresentable {
    EVERYWHERE("Everywhere"),
    TITLE_ONLY("Title Only"),
    OFF("OFF");

    private final String name;

    MenuButton(final String name) {
        this.name = name;
    }

    public boolean enabled() {
        return this != OFF;
    }

    public boolean everywhere() {
        return this != OFF && this != TITLE_ONLY;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
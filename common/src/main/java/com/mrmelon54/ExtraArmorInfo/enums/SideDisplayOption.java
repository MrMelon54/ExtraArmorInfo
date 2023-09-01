package com.mrmelon54.ExtraArmorInfo.enums;

public enum SideDisplayOption {
    LEFT("Left"),
    RIGHT("Right");

    private final String name;

    SideDisplayOption(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

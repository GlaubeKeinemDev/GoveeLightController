package de.glaubekeinemdev.goveelightcontroller;

public enum UpdateType {

    ENABLE("turn"),
    BRIGHTNESS("brightness"),
    COLOR("color"),
    TEMPERATURE("colorTem");

    private final String name;

    UpdateType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

package de.glaubekeinemdev.goveelightcontroller;

import java.awt.*;

public class Device {

    private final String model;
    private final String macAdress;
    private final String name;
    private boolean enabled;
    private int brightness;
    private Color color;

    public Device(String model, String macAdress, String name, boolean enabled, int brightness, Color color) {
        this.model = model;
        this.macAdress = macAdress;
        this.name = name;
        this.enabled = enabled;
        this.brightness = brightness;
        this.color = color;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getBrightness() {
        return brightness;
    }

    public Color getColor() {
        return color;
    }

    public String getModel() {
        return model;
    }

    public String getMacAdress() {
        return macAdress;
    }

    public String getName() {
        return name;
    }

    public boolean enableDevice(final boolean status) {
        final boolean result = GoveeLightController.getInstance().updateDevice(this, UpdateType.ENABLE, status);

        if(result)
            this.enabled = status;

        return result;
    }

    public boolean changeColor(final Color color) {
        final boolean result = GoveeLightController.getInstance().updateDevice(this, UpdateType.COLOR, color);

        if(result)
            this.color = color;

        return result;
    }

    public boolean changeBrightness(final int brightness) {
        final boolean result = GoveeLightController.getInstance().updateDevice(this, UpdateType.BRIGHTNESS, brightness);

        if(result)
            this.brightness = brightness;

        return result;
    }

    public boolean changeTemperature(final int temperature) {
        return GoveeLightController.getInstance().updateDevice(this, UpdateType.TEMPERATURE, temperature);
    }
}

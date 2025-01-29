package org.moss.extraoverlay.client.overlay;
public class OverlaySetting<T> {
    private final String id;
    private final String name;
    private final SettingType type;
    private T value;
    private final T defaultValue;
    
    public enum SettingType {
        TOGGLE,
        COLOR,
        SLIDER,
        SELECT
    }
    
    public OverlaySetting(String id, String name, SettingType type, T defaultValue) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
    }

    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public SettingType getType() {
        return type;
    }
    
    public T getValue() {
        return value;
    }
    
    public void setValue(T value) {
        this.value = value;
    }
}
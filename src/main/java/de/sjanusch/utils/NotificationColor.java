package de.sjanusch.utils;

public enum NotificationColor {

    YELLOW("yellow"),

    GREEN("green"),
    
    RED("red"),
    
    PURPLE("purple"),
    
    GRAY("gray"),
    
    RANDOM("random");

    String type;
    NotificationColor(String type) { this.type = type; }

    public String getType() { return type; }
}

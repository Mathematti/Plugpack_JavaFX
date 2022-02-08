package com.mathematti.plugpack;

public class SpigotPlugin extends Plugin {
    private int id;

    public SpigotPlugin(String name, String type, String link) {
        super(name, type);
        this.id = Integer.parseInt(link.substring(link.lastIndexOf('.'), link.length() - 1));
    }
}

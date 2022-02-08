package com.mathematti.plugpack.plugin;

public class SpigotPlugin extends Plugin {
    private int id;

    public SpigotPlugin(String name, String link) {
        super(name);
        this.id = Integer.parseInt(link.substring(link.lastIndexOf('.'), link.length() - 1));
    }

    @Override
    public String download() {
        return id + "";
    }
}

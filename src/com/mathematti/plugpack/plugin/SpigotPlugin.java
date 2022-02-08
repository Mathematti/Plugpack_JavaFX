package com.mathematti.plugpack.plugin;

public class SpigotPlugin extends Plugin {
    final private int id;
    final private String link;

    public SpigotPlugin(String name, String type, String link) {
        super(name, type);
        this.id = Integer.parseInt(link.substring(link.lastIndexOf('.') + 1, link.length() - 1));
        this.link = link;
    }

    @Override
    public String download() {
        return link;
    }

    public int getId() {
        return id;
    }
}

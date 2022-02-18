package com.mathematti.plugpack.functions.plugin;

public abstract class Plugin {
    final private String name;
    final private String type;

    public Plugin(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public abstract String download();

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}

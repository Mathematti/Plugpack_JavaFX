package com.mathematti.plugpack.plugin;

public abstract class Plugin {
    private String name;

    public Plugin(String name) {
        this.name = name;
    }

    public abstract String download();
}

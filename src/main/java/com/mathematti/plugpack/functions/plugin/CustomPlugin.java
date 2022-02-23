package com.mathematti.plugpack.functions.plugin;

public class CustomPlugin extends Plugin {
    final private String downloadCommand;

    public CustomPlugin(String name, String type, String command) {
        super(name, type);
        downloadCommand = command;
    }

    @Override
    public String download() {
        return downloadCommand;
    }
}

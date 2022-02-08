package com.mathematti.plugpack.plugin;

public class CustomPlugin extends Plugin {
    private String downloadCommand;

    public CustomPlugin(String name, String command) {
        super(name);
        downloadCommand = command;
    }

    @Override
    public String download() {
        return downloadCommand;
    }
}

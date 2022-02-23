package com.mathematti.plugpack.functions.plugin;

public class DirectPlugin extends Plugin {
    String downloadLink;

    public DirectPlugin(String name, String type, String downloadLink) {
        super(name, type);
        this.downloadLink = downloadLink;
    }

    @Override
    public String download() {
        return downloadLink;
    }
}

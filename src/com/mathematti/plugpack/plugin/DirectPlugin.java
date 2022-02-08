package com.mathematti.plugpack.plugin;

public class DirectPlugin extends Plugin {
    String downloadLink;
    public DirectPlugin(String name, String downloadLink) {
        super(name);
        this.downloadLink = downloadLink;
    }

    @Override
    public String download() {
        return downloadLink;
    }
}

package com.mathematti.plugpack.plugin;

public class BukkitPlugin extends Plugin {
    String downloadLink;

    public BukkitPlugin(String name, String link) {
        super(name);

        if (link.contains("files/latest")) {
            downloadLink = link;
        } else if (link.endsWith("/")) {
            downloadLink = link + "files/latest";
        } else {
            downloadLink = link + "/files/latest";
        }
    }

    @Override
    public String download() {
        return downloadLink;
    }
}

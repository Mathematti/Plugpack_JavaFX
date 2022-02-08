package com.mathematti.plugpack;

public class BukkitPlugin extends Plugin {
    String downloadLink;

    public BukkitPlugin(String name, String type, String link) {
        super(name, type);

        if (link.contains("files/latest")) {
            downloadLink = link;
        } else if (link.endsWith("/")) {
            downloadLink = link + "files/latest";
        } else {
            downloadLink = link + "/files/latest";
        }
    }
}

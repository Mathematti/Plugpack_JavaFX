package com.mathematti.plugpack.plugin;

public class BukkitPlugin extends DirectPlugin {
    public BukkitPlugin(String name, String type, String link) {
        super(name, type, getLink(link));
    }

    private static String getLink(String link) {
        if (link.contains("files/latest")) {
            return link;
        } else if (link.endsWith("/")) {
            return link + "files/latest";
        } else {
            return link + "/files/latest";
        }
    }
}

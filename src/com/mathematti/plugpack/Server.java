package com.mathematti.plugpack;

import com.mathematti.plugpack.plugin.BukkitPlugin;
import com.mathematti.plugpack.plugin.DirectPlugin;
import com.mathematti.plugpack.plugin.Plugin;
import com.mathematti.plugpack.plugin.SpigotPlugin;

import java.util.Arrays;

public class Server {
    public static Server[] servers = new Server[0];

    final private String serverName;
    private Plugin[] plugins = new Plugin[0];

    public Server(String name) {
        serverName = name;
    }

    public String getName() {
        return serverName;
    }

    public void addPlugin(String name, String type, String link) {
        Plugin plugin = null;
        if (type.equalsIgnoreCase("spigot")) {
            plugin = new SpigotPlugin(name, link);
        } else if (type.equalsIgnoreCase("bukkit")) {
            plugin = new BukkitPlugin(name, link);
        } else if (type.equalsIgnoreCase("direct")) {
            plugin = new DirectPlugin(name, link);
        } else if (type.equalsIgnoreCase("custom")) {
            plugin = new DirectPlugin(name, link);
        }

        plugins = Arrays.copyOf(plugins, plugins.length + 1);
        plugins[plugins.length - 1] = plugin;
    }
}

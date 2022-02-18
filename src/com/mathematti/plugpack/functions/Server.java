package com.mathematti.plugpack.functions;

import com.mathematti.plugpack.functions.plugin.*;
import com.mathematti.plugpack.gui.AddServerGUI;
import com.mathematti.plugpack.gui.ModifyServerGUI;
import javafx.stage.Stage;

import java.util.Arrays;

public class Server {
    public static Server[] servers = new Server[0];

    final private String serverName;
    public Plugin[] plugins = new Plugin[0];

    public Server(String name) {
        serverName = name;
    }

    public String getName() {
        return serverName;
    }

    public static void addServer(String name) {
        servers = Arrays.copyOf(servers, servers.length + 1);
        servers[servers.length - 1] = new Server(name);
    }

    public void addPlugin(String name, String type, String link) {
        Plugin plugin = null;
        if (type.equalsIgnoreCase("spigot")) {
            plugin = new SpigotPlugin(name, type, link);
        } else if (type.equalsIgnoreCase("bukkit")) {
            plugin = new BukkitPlugin(name, type, link);
        } else if (type.equalsIgnoreCase("direct")) {
            plugin = new DirectPlugin(name, type, link);
        } else if (type.equalsIgnoreCase("custom")) {
            plugin = new CustomPlugin(name, type, link);
        }

        plugins = Arrays.copyOf(plugins, plugins.length + 1);
        plugins[plugins.length - 1] = plugin;
    }

    public static void checkServer(Stage stage, String name) {
        boolean error = false;
        if (name.equals("")) {
            AddServerGUI.addServerGUI(stage, "empty");
            error = true;
        }
        for (Server server : servers) {
            if (server.getName().equals(name)) {
                AddServerGUI.addServerGUI(stage, "exists");
                error = true;
            }
        }
        if (!error) {
            addServer(name);
            ModifyServerGUI.modifyServerGUI(stage, name);
        }
    }
}

package com.mathematti.plugpack.functions;

import com.mathematti.plugpack.functions.plugin.SortByName;
import javafx.scene.control.TextArea;

import java.util.Arrays;

public class ImportScript {
    public static void importScript(TextArea scriptInput) {
        String script = scriptInput.getText();
        String serversString =
                script.substring(script.indexOf("# Servers: ") + "# Servers: ".length(),
                        script.indexOf(" ServersEnd"));
        String[] servers = serversString.split(",");

        for (String server : servers) {
            Server.addServer(server);

            String spigotPluginString =
                    script.substring(
                            (script.indexOf("# " + server + "-SpigotPlugins: ")
                                    + ("# " + server + "-SpigotPlugins: ").length()),
                            script.indexOf(" " + server + "-SpigotPluginsEnd")
                    );
            String[] spigotPlugins = spigotPluginString.split(",");
            for (String plugin : spigotPlugins) {
                try {
                    Server.servers[Server.servers.length - 1].
                            addPlugin(plugin.substring(0, plugin.indexOf(":")), "spigot",
                                    plugin.substring(plugin.indexOf(":") + 1));
                } catch (StringIndexOutOfBoundsException ignored) {
                }

            }

            String directPluginString =
                    script.substring(
                            (script.indexOf("# " + server + "-DirectPlugins: ")
                                    + ("# " + server + "-DirectPlugins: ").length()),
                            script.indexOf(" " + server + "-DirectPluginsEnd")
                    );
            String[] directPlugins = directPluginString.split(",");
            for (String plugin : directPlugins) {
                try {
                    Server.servers[Server.servers.length - 1].
                            addPlugin(plugin.substring(0, plugin.indexOf(":")), "direct",
                                    plugin.substring(plugin.indexOf(":") + 1));
                } catch (StringIndexOutOfBoundsException ignored) {
                }
            }

            String customPluginString =
                    script.substring(
                            (script.indexOf("# " + server + "-CustomPlugins: ")
                                    + ("# " + server + "-CustomPlugins: ").length()),
                            script.indexOf(" " + server + "-CustomPluginsEnd")
                    );
            String[] customPlugins = customPluginString.split("@plcs@");
            for (String plugin : customPlugins) {
                try {
                    Server.servers[Server.servers.length - 1].
                            addPlugin(plugin.substring(0, plugin.indexOf(":")),
                                    "custom",
                                    plugin.substring(plugin.indexOf(":") + 1)
                                            .replaceAll("@plclb@", "\n"));
                } catch (StringIndexOutOfBoundsException ignored) {
                }
            }
        }

        for (Server server : Server.servers) {
            Arrays.sort(server.plugins, new SortByName());
        }
    }
}
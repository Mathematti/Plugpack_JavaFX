package com.mathematti.plugpack.functions;

import com.mathematti.plugpack.functions.plugin.CustomPlugin;
import com.mathematti.plugpack.functions.plugin.DirectPlugin;
import com.mathematti.plugpack.functions.plugin.Plugin;
import com.mathematti.plugpack.functions.plugin.SpigotPlugin;

public class GenerateScript {
    public static String generateScript() {
        StringBuilder config = new StringBuilder("# Servers: ");
        for (Server server : Server.servers) {
            config.append(server.getName()).append(",");
        }
        config.append(" ServersEnd\n");

        for (Server server : Server.servers) {
            config.append("# ").append(server.getName()).append("-SpigotPlugins: ");
            for (Plugin plugin : server.plugins) {
                if (plugin instanceof SpigotPlugin) {
                    config.append(plugin.getName()).append(":")
                            .append(".").append(((SpigotPlugin) plugin).getId()).append("/").append(",");
                }
            }
            config.append(" ").append(server.getName()).append("-SpigotPluginsEnd").append("\n");

            config.append("# ").append(server.getName()).append("-DirectPlugins: ");
            for (Plugin plugin : server.plugins) {
                if (plugin instanceof DirectPlugin) {
                    config.append(plugin.getName()).append(":").append(plugin.download()).append(",");
                }
            }
            config.append(" ").append(server.getName()).append("-DirectPluginsEnd").append("\n");

            config.append("# ").append(server.getName()).append("-CustomPlugins: ");
            for (Plugin plugin : server.plugins) {
                if (plugin instanceof CustomPlugin) {
                    config.append(plugin.getName()).append(":")
                            .append(plugin.download().replaceAll("\n", "@plclb@")).append("@plcs@");
                }
            }
            config.append(" ").append(server.getName()).append("-CustomPluginsEnd").append("\n");
        }
        config.append("\n");
        config.append("sudo rm -f -r /var/lib/Plugpack/*\n");
        config.append("\n");

        StringBuilder output = new StringBuilder(config.toString());


        for (Server server : Server.servers) {
            output.append("sudo mkdir -p /var/lib/Plugpack/plugins/").append(server.getName()).append("/\n");
            output.append("cd /var/lib/Plugpack/plugins/").append(server.getName()).append("/\n");
            for (Plugin plugin : server.plugins) {
                if (plugin instanceof DirectPlugin) {
                    String pluginLink = plugin.download();

                    output.append("sudo wget ").append(pluginLink).append("\n");

                    if (pluginLink.endsWith("/")) {
                        pluginLink = pluginLink.substring(0, pluginLink.length() - 1);
                    }
                    output.append("sudo mv ./").append(pluginLink.substring(pluginLink.lastIndexOf("/") + 1))
                            .append(" ./").append(plugin.getName().replaceAll(" ", ""))
                            .append(".jar\n");
                } else if (plugin instanceof CustomPlugin) {
                    output.append(plugin.download()).append("\n");
                } else if (plugin instanceof SpigotPlugin) {
                    output.append("sudo wget https://api.spiget.org/v2/resources/")
                            .append(((SpigotPlugin) plugin).getId())
                            .append("/download\n");
                    output.append("sudo mv ./download ./")
                            .append(plugin.getName().replaceAll(" ", ""))
                            .append(".jar\n");
                }
            }

            output.append("cd ../../../\n");
        }

        output.append("sudo mkdir /var/lib/Plugpack/out/\n");

        for (Server server : Server.servers) {
            output.append("cd /var/lib/Plugpack/plugins/").append(server.getName()).append("/\n");
            output.append("sudo zip ../../out/").append(server.getName())
                    .append(".zip ./*\n");
            output.append("cd ../../../\n");
        }

        return output.toString();
    }
}
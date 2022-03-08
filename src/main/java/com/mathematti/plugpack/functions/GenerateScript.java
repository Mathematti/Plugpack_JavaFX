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

        String[] pluginServers = new String[Server.servers.length];

        for (int i = 0; i < Server.servers.length; i++) {
            StringBuilder spigotIDs = new StringBuilder();

            for (Plugin plugin : Server.servers[i].plugins) {
                if (plugin instanceof SpigotPlugin) {
                    if (spigotIDs.toString().equals("")) {
                        spigotIDs.append(((SpigotPlugin) plugin).getId());
                    } else {
                        spigotIDs.append(",").append(((SpigotPlugin) plugin).getId());
                    }
                }
            }

            pluginServers[i] = "sudo docker run -d --rm"
                    + " --dns 8.8.8.8"
                    + " -v /var/lib/Plugpack/" + Server.servers[i].getName() + ":/data"
                    + " --name plugpack_" + Server.servers[i].getName()
                    + " -e TYPE=PAPER"
                    + " -e EULA=true"
                    + " -e REMOVE_OLD_MODS=TRUE"
                    + " -e SPIGET_RESOURCES=" + spigotIDs
                    + "\" itzg/minecraft-server\n";

            pluginServers[i] += "while [ \"`sudo docker inspect -f {{.State.Health.Status}} plugpack_"
                    + Server.servers[i].getName() + "`\" != \"healthy\" ]; do sleep 2; done\n";
            pluginServers[i] += "sudo docker stop plugpack_" + Server.servers[i].getName() + "\n";
            pluginServers[i] += "sudo mkdir -p /var/lib/Plugpack/plugins/" + Server.servers[i].getName() + "/";
        }

        StringBuilder output = new StringBuilder(config.toString());

        for (String pluginServer : pluginServers) {
            output.append(pluginServer).append("\n");
        }

        for (Server server : Server.servers) {
            output.append("cd /var/lib/Plugpack/").append(server.getName()).append("/plugins/\n");
            for (Plugin plugin : server.plugins) {
                if (plugin instanceof DirectPlugin) {
                    String pluginLink = plugin.download();

                    output.append("sudo wget ").append(pluginLink).append("\n");

                    if (pluginLink.endsWith("/")) {
                        pluginLink = pluginLink.substring(0, pluginLink.length() - 1);
                    }
                    output.append("sudo mv ./").append(pluginLink.substring(pluginLink.lastIndexOf("/") + 1))
                            .append(" ./").append(plugin.getName()).append(".jar\n");
                }
            }

            for (Plugin plugin : server.plugins) {
                if (plugin instanceof CustomPlugin) {
                    output.append(plugin.download()).append("\n");
                }
            }
            output.append("cd ../../../\n");
        }

        for (Server server : Server.servers) {
            for (Plugin plugin : server.plugins) {
                if (plugin instanceof SpigotPlugin) {
                    output.append("if [ -f /var/lib/Plugpack/").append(server.getName()).append("/plugins/")
                            .append(((SpigotPlugin) plugin).getId()).append(".jar").append(" ]; then\n");
                    output.append("    sudo mv /var/lib/Plugpack/").append(server.getName()).append("/plugins/")
                            .append(((SpigotPlugin) plugin).getId()).append(".jar")
                            .append(" /var/lib/Plugpack/plugins/").append(server.getName()).append("/")
                            .append(plugin.getName().replaceAll(" ", "")).append(".jar\n");
                    output.append("fi\n");
                }
            }
            output.append("sudo mv /var/lib/Plugpack/").append(server.getName())
                    .append("/plugins/*.jar /var/lib/Plugpack/plugins/").append(server.getName()).append("/\n");
        }

        output.append("sudo mkdir /var/lib/Plugpack/out/\n");

        for (Server server : Server.servers) {
            output.append("cd /var/lib/Plugpack/plugins/").append(server.getName()).append("/\n");
            output.append("sudo zip ../../out/").append(server.getName())
                    .append(".zip ./*.jar\n");
            output.append("cd ../../../\n");
        }

        return output.toString();
    }
}
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

        String[] pluginServers = new String[Server.servers.length];

        for (int i = 0; i < Server.servers.length; i++) {
            StringBuilder spigotIDs = new StringBuilder();
            StringBuilder directURLs = new StringBuilder();

            for (Plugin plugin : Server.servers[i].plugins) {
                if (plugin instanceof SpigotPlugin) {
                    if (spigotIDs.toString().equals("")) {
                        spigotIDs.append(((SpigotPlugin) plugin).getId());
                    } else {
                        spigotIDs.append(",").append(((SpigotPlugin) plugin).getId());
                    }
                } else if (plugin instanceof DirectPlugin) {
                    if (directURLs.toString().equals("")) {
                        directURLs.append(plugin.download());
                    } else {
                        directURLs.append(",").append(plugin.download());
                    }
                }
            }

            pluginServers[i] = "sudo docker run -d --rm"
                    + " --dns 8.8.8.8"
                    + " -v $PWD/Plugpack/" + Server.servers[i].getName() + ":/data"
                    + " --name plugpack_" + Server.servers[i].getName()
                    + " -e TYPE=PAPER"
                    + " -e EULA=true"
                    + " -e REMOVE_OLD_MODS=TRUE"
                    + " -e SPIGET_RESOURCES=" + spigotIDs
                    + " -e MODS=" + directURLs
                    + " itzg/minecraft-server\n";

            pluginServers[i] += "while [ \"`sudo docker inspect -f {{.State.Health.Status}} plugpack_"
                    + Server.servers[i].getName() + "`\" != \"healthy\" ]; do sleep 2; done\n";
            pluginServers[i] += "sudo docker stop plugpack_" + Server.servers[i].getName() + "\n";
            pluginServers[i] += "sudo mkdir -p ./Plugpack/plugins/" + Server.servers[i].getName() + "/";
        }

        StringBuilder output = new StringBuilder(config.toString());

        for (String pluginServer : pluginServers) {
            output.append(pluginServer).append("\n");
        }

        for (Server server : Server.servers) {
            output.append("cd ./Plugpack/").append(server.getName()).append("/plugins/\n");
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
                    output.append("if [ -f ./Plugpack/").append(server.getName()).append("/plugins/")
                            .append(((SpigotPlugin) plugin).getId()).append(".jar").append(" ]; then\n");
                    output.append("    sudo mv ./Plugpack/").append(server.getName()).append("/plugins/")
                            .append(((SpigotPlugin) plugin).getId()).append(".jar")
                            .append(" ./Plugpack/plugins/").append(server.getName()).append("/")
                            .append(plugin.getName().replaceAll(" ", "")).append(".jar\n");
                    output.append("fi\n");
                }
            }
            output.append("sudo mv ./Plugpack/").append(server.getName())
                    .append("/plugins/*.jar ./Plugpack/plugins/").append(server.getName()).append("/\n");
        }

        output.append("sudo mkdir ./Plugpack/out/\n");

        for (Server server : Server.servers) {
            output.append("cd ./Plugpack/plugins/").append(server.getName()).append("/\n");
            output.append("sudo zip ../../out/").append(server.getName())
                    .append(".zip ./*.jar\n");
            output.append("cd ../../../");
        }

        return output.toString();
    }
}
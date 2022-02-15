package com.mathematti.plugpack.gui;

import com.mathematti.plugpack.Server;
import com.mathematti.plugpack.plugin.BukkitPlugin;
import com.mathematti.plugpack.plugin.DirectPlugin;
import com.mathematti.plugpack.plugin.Plugin;
import com.mathematti.plugpack.plugin.SpigotPlugin;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GenerateScript {
    public static void generateScriptGUI(Stage stage) {
        stage.setTitle("Plugpack - Generate script");

        final int padding = 10;
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(padding));
        vBox.setSpacing(padding);

        final int width = 350;
        final int height = 200;
        stage.setScene(new Scene(vBox, width, height));

        Label output = new Label("Here's your script:");
        TextArea script = new TextArea();
        Button cancel = new Button("Cancel");
        cancel.setOnAction(actionEvent -> MainMenu.showMainGUI(stage));

        vBox.getChildren().addAll(output, script, cancel);

        script.setText(generateScript());
    }

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
            for (Plugin plugin : server.plugins) {
                if (plugin instanceof SpigotPlugin) {
                    output.append("sudo mv ./Plugpack/").append(server.getName()).append("/plugins/")
                            .append(((SpigotPlugin) plugin).getId()).append(".jar")
                            .append(" ./Plugpack/plugins/").append(server.getName()).append("/")
                            .append(plugin.getName()).append(".jar\n");
                }
            }
            output.append("sudo mv ./Plugpack/").append(server.getName())
                    .append("/plugins/*.jar ./Plugpack/plugins/").append(server.getName()).append("/\n");
        }

        output.append("sudo mkdir ./Plugpack/out/\n");

        for (Server server : Server.servers) {
            output.append("sudo zip ./Plugpack/out/").append(server.getName())
                    .append(".zip ./Plugpack/plugins/").append(server.getName()).append("/*.jar");
        }

        return output.toString();
    }
}

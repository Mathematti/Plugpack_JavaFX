package com.mathematti.plugpack.gui;

import com.mathematti.plugpack.Server;
import com.mathematti.plugpack.plugin.BukkitPlugin;
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
        String[] pluginServers = new String[Server.servers.length];

        for (int i = 0; i < Server.servers.length; i++) {
            String spigotIDs = "";
            String directURLs = "";

            for (Plugin plugin : Server.servers[i].plugins) {
                if (plugin instanceof SpigotPlugin) {
                    if (spigotIDs.equals("")) {
                        spigotIDs = ((SpigotPlugin) plugin).getId() + "";
                    } else {
                        spigotIDs = "," + ((SpigotPlugin) plugin).getId();
                    }
                } else if (plugin instanceof BukkitPlugin) {
                    if (directURLs.equals("")) {
                        directURLs = plugin.download();
                    } else {
                        directURLs = "," + plugin.download();
                    }
                }
            }

            pluginServers[i] = "docker run -d --rm"
                    + " $PWD/Plugpack/" + Server.servers[i].getName() + ":/data"
                    + " -e EULA=true"
                    + " -e SPIGET_RESOURCES=" + spigotIDs
                    + " -e MODS=" + directURLs
                    + " itzg/minecraft-server";
        }

        StringBuilder output = new StringBuilder();

        for (String pluginServer : pluginServers) {
            output.append(pluginServer).append("\n");
        }

        return output.toString();
    }
}

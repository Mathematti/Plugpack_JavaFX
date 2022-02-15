package com.mathematti.plugpack.gui;

import com.mathematti.plugpack.Server;
import com.mathematti.plugpack.plugin.SortByName;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;

public class ImportScript {
    public static void importScriptGUI(Stage stage) {
        stage.setTitle("Plugpack - Import script");

        final int padding = 10;
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(padding));
        vBox.setSpacing(padding);

        final int width = 350;
        final int height = 200;
        stage.setScene(new Scene(vBox, width, height));

        Label output = new Label("Paste your script here:");
        TextArea script = new TextArea();
        Button cancel = new Button("Cancel");
        cancel.setOnAction(actionEvent -> MainMenu.showMainGUI(stage));
        Button importScript = new Button("Import");
        importScript.setOnAction(actionEvent -> {
            importScript(script);
            MainMenu.showMainGUI(stage);
        });

        HBox hBox = new HBox();
        hBox.setSpacing(padding);

        hBox.getChildren().addAll(cancel, importScript);
        vBox.getChildren().addAll(output, script, hBox);
    }

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
                } catch (StringIndexOutOfBoundsException ignored) {}

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
                } catch (StringIndexOutOfBoundsException ignored) {}
            }
        }

        for (Server server : Server.servers) {
            Arrays.sort(server.plugins, new SortByName());
        }
    }
}

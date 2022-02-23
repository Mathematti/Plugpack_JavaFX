package com.mathematti.plugpack.gui;

import com.mathematti.plugpack.functions.Server;
import com.mathematti.plugpack.functions.plugin.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;

public class ModifyPluginsGUI {
    public static void modifyPluginGUI(Stage stage, Server server, Plugin plugin) {
        stage.setTitle("Plugpack - Modify " + plugin.getName());

        final int padding = 10;
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(padding));
        vBox.setSpacing(padding);

        HBox hBox = new HBox();
        hBox.setSpacing(padding);

        int width = 350;

        Label top = new Label("Modify " + plugin.getName() + ":");
        Label pluginName = new Label("Plugin name:");
        TextField pluginNameInput = new TextField(plugin.getName());
        Label pluginType = new Label("Type");
        TextField pluginTypeInput = new TextField(plugin.getType());
        Label pluginLink = new Label("Download link:");
        TextField pluginLinkInput = new TextField(plugin.download());
        TextArea pluginCommandInput = new TextArea(plugin.download());

        Button cancel = new Button("Cancel");
        cancel.setOnAction(actionEvent -> ChoosePluginGUI.choosePluginGUI(stage, server));

        Button save = new Button("Save");
        save.setOnAction(actionEvent -> {
            for (int i = 0; i < server.plugins.length; i++) {
                if (server.plugins[i].getName().equals(plugin.getName())) {
                    if (pluginTypeInput.getText().equalsIgnoreCase("spigot")) {
                        server.plugins[i] = new SpigotPlugin(
                                pluginNameInput.getText(), pluginTypeInput.getText(), pluginLinkInput.getText());
                    } else if (pluginTypeInput.getText().equalsIgnoreCase("bukkit")) {
                        server.plugins[i] = new BukkitPlugin(
                                pluginNameInput.getText(), pluginTypeInput.getText(), pluginLinkInput.getText());
                    } else if (pluginTypeInput.getText().equalsIgnoreCase("direct")) {
                        server.plugins[i] = new DirectPlugin(
                                pluginNameInput.getText(), pluginTypeInput.getText(), pluginLinkInput.getText());
                    } else if (pluginTypeInput.getText().equalsIgnoreCase("custom")) {
                        server.plugins[i] = new DirectPlugin(
                                pluginNameInput.getText(), pluginTypeInput.getText(), pluginCommandInput.getText());
                    }
                }
            }
            Arrays.sort(server.plugins, new SortByName());
            ChoosePluginGUI.choosePluginGUI(stage, server);
        });

        Button delete = new Button("Delete");
        delete.setOnAction(actionEvent -> {
            Plugin[] temp = server.plugins.clone();
            server.plugins = new Plugin[temp.length - 1];

            int placeAt = 0;
            for (Plugin currentPlugin : temp) {
                if (!currentPlugin.getName().equals(plugin.getName())) {
                    server.plugins[placeAt] = currentPlugin;
                    placeAt++;
                }
            }
            ChoosePluginGUI.choosePluginGUI(stage, server);
        });

        hBox.getChildren().addAll(cancel, save, delete);

        if (plugin.getType().equals("custom")) {
            stage.setScene(new Scene(vBox, width, 400));
            vBox.getChildren().addAll(top, pluginName, pluginNameInput, pluginType, pluginTypeInput,
                    pluginLink, pluginCommandInput, hBox);
        } else {
            stage.setScene(new Scene(vBox, width, 300));
            vBox.getChildren().addAll(top, pluginName, pluginNameInput, pluginType, pluginTypeInput,
                    pluginLink, pluginLinkInput, hBox);
        }
    }
}

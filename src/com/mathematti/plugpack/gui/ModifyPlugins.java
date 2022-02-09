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
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ModifyPlugins {
    public static void choosePluginGUI(Stage stage, Server server) {
        stage.setTitle("Plugpack - Modify plugins for " + server.getName());

        final int padding = 10;
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(padding));
        vBox.setSpacing(padding);

        FlowPane flowPane = new FlowPane();
        flowPane.setVgap(padding);
        flowPane.setHgap(padding);

        int width = 350;
        final int height = 200;
        stage.setScene(new Scene(vBox, width, height));

        Label top = new Label("Which plugin do you want to modify?");
        Button cancel = new Button("Cancel");
        cancel.setOnAction(actionEvent -> ModifyServer.modifyServerGUI(stage, server.getName()));
        Button addPlugin = new Button("Add plugin");
        addPlugin.setOnAction(actionEvent -> addPluginGUI(stage, server));

        List<Button> buttonList = new ArrayList<>();

        for (Plugin plugin : server.plugins) {
            buttonList.add(new Button(plugin.getName()));
        }

        for (int i = 0; i < buttonList.size(); i++) {
            int finalI = i;
            buttonList.get(i).setOnAction(actionEvent -> modifyPluginGUI(stage, server, server.plugins[finalI]));
        }

        HBox hBox = new HBox();
        hBox.setSpacing(padding);

        hBox.getChildren().addAll(cancel, addPlugin);
        flowPane.getChildren().addAll(buttonList);
        vBox.getChildren().addAll(top, flowPane, hBox);
    }

    public static void addPluginGUI(Stage stage, Server server) {
        stage.setTitle("Plugpack - Add new plugin");

        final int padding = 10;
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(padding));
        vBox.setSpacing(padding);

        final int width = 350;
        final int height = 200;
        stage.setScene(new Scene(vBox, width, height));

        AtomicReference<String> pluginName = new AtomicReference<>("");
        AtomicReference<String> pluginType = new AtomicReference<>("");
        AtomicReference<String> downloadLink = new AtomicReference<>("");
        AtomicInteger question = new AtomicInteger(1);

        Label inputLabel = new Label("Input plugin name:");
        TextField input = new TextField();
        Button confirm = new Button("Confirm");
        confirm.setOnAction(actionEvent -> {
            if (question.get() == 1) {
                inputLabel.setText("Input plugin type (spigot, bukkit, direct, custom):");
                pluginName.set(input.getText());
                input.clear();
            } else if (question.get() == 2) {
                inputLabel.setText("Input plugin download link:");
                pluginType.set(input.getText());
                input.clear();
            } else if (question.get() == 3) {
                downloadLink.set(input.getText());
                input.clear();
                server.addPlugin(pluginName.get(), pluginType.get(), downloadLink.get());
                choosePluginGUI(stage, server);
            }
            question.getAndIncrement();
        });

        vBox.getChildren().addAll(inputLabel, input, confirm);
    }

    public static void modifyPluginGUI(Stage stage, Server server, Plugin plugin) {
        stage.setTitle("Plugpack - Modify " + plugin.getName());

        final int padding = 10;
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(padding));
        vBox.setSpacing(padding);

        HBox hBox = new HBox();
        hBox.setSpacing(padding);

        int width = 350;
        final int height = 300;
        stage.setScene(new Scene(vBox, width, height));

        Label top = new Label("Modify " + plugin.getName() + ":");
        Label pluginName = new Label("Plugin name:");
        TextField pluginNameInput = new TextField(plugin.getName());
        Label pluginType = new Label("Type");
        TextField pluginTypeInput = new TextField(plugin.getType());
        Label pluginLink = new Label("Download link:");
        TextField pluginLinkInput = new TextField(plugin.download());

        Button cancel = new Button("Cancel");
        cancel.setOnAction(actionEvent -> choosePluginGUI(stage, server));

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
                                pluginNameInput.getText(), pluginTypeInput.getText(), pluginLinkInput.getText());
                    }
                }
            }
            choosePluginGUI(stage, server);
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
            choosePluginGUI(stage, server);
        });

        hBox.getChildren().addAll(cancel, save, delete);

        vBox.getChildren().addAll(top, pluginName, pluginNameInput, pluginType, pluginTypeInput,
                pluginLink, pluginLinkInput, hBox);
    }
}

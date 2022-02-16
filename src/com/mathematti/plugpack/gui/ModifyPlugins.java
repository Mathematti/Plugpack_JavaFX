package com.mathematti.plugpack.gui;

import com.mathematti.plugpack.Server;
import com.mathematti.plugpack.plugin.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
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

        HBox hyperlinks = new HBox();
        hyperlinks.setPadding(new Insets(padding));
        hyperlinks.setSpacing(padding);

        Label top = new Label("Which plugin do you want to modify?");
        Button cancel = new Button("Cancel");
        cancel.setOnAction(actionEvent -> ModifyServer.modifyServerGUI(stage, server.getName()));
        Button addPlugin = new Button("Add plugin");
        addPlugin.setOnAction(actionEvent -> addPluginGUI(stage, server));

        List<Hyperlink> hyperlinkList = new ArrayList<>();

        for (Plugin plugin : server.plugins) {
            hyperlinkList.add(new Hyperlink(plugin.getName()));
        }

        for (int i = 0; i < hyperlinkList.size(); i++) {
            int finalI = i;
            hyperlinkList.get(i).setOnAction(actionEvent -> modifyPluginGUI(stage, server, server.plugins[finalI]));
        }

        final int columns = (int) Math.ceil(server.plugins.length / (float) 10);

        List<VBox> containers = new ArrayList<>();

        for (int i = 0; i < columns; i++) {
            containers.add(new VBox());
            containers.get(i).setPadding(new Insets(padding));
            containers.get(i).setSpacing(padding);

            for (int j = 0; j < Math.ceil(server.plugins.length / (float) columns); j++) {
                try {
                    containers.get(i).getChildren().addAll(hyperlinkList
                            .get((int) (i * Math.ceil(server.plugins.length / (float) columns) + j)));
                } catch (Exception ignored) {}
            }
        }

        HBox hBox = new HBox();
        hBox.setSpacing(padding);

        hBox.getChildren().addAll(cancel, addPlugin);
        hyperlinks.getChildren().addAll(containers);
        vBox.getChildren().addAll(top, hyperlinks, hBox);

        double width = columns * 140;
        if (width < 350) {
            width = 350;
        }

        double height = Math.ceil(server.plugins.length / (float) columns) * 35 + 105;
        if (height < 200) {
            height = 200;
        }

        stage.setScene(new Scene(vBox, width, height));
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

        final ToggleGroup group = new ToggleGroup();
        RadioButton spigot = new RadioButton("Spigot");
        spigot.setToggleGroup(group);
        RadioButton bukkit = new RadioButton("Bukkit");
        bukkit.setToggleGroup(group);
        RadioButton direct = new RadioButton("Direct");
        direct.setToggleGroup(group);
        RadioButton custom = new RadioButton("Custom");
        custom.setToggleGroup(group);

        AtomicReference<String> pluginName = new AtomicReference<>("");
        AtomicReference<String> pluginType = new AtomicReference<>("");
        AtomicReference<String> downloadLink = new AtomicReference<>("");
        AtomicInteger question = new AtomicInteger(1);

        Label inputLabel = new Label("Input plugin name:");
        TextField input = new TextField();
        Button confirm = new Button("Confirm");
        TextArea inputArea = new TextArea();
        confirm.setOnAction(actionEvent -> {
            if (question.get() == 1) {
                inputLabel.setText("Input plugin type:");
                vBox.getChildren().clear();
                vBox.getChildren().addAll(inputLabel, spigot, bukkit, direct, custom, confirm);
                pluginName.set(input.getText());
                input.clear();
            } else if (question.get() == 2) {
                inputLabel.setText("Input plugin download link:");
                vBox.getChildren().clear();
                vBox.getChildren().addAll(inputLabel, input, confirm);
                if (spigot.isSelected()) {
                    pluginType.set("spigot");
                } else if (bukkit.isSelected()) {
                    pluginType.set("bukkit");
                } else if (direct.isSelected()) {
                    pluginType.set("direct");
                } else if (custom.isSelected()) {
                    pluginType.set("custom");
                    vBox.getChildren().clear();
                    vBox.getChildren().addAll(inputLabel, inputArea, confirm);
                    inputLabel.setText("Input plugin download command(s):");
                }
                input.clear();
            } else if (question.get() == 3) {
                if (pluginType.get().equalsIgnoreCase("custom")) {
                    downloadLink.set(inputArea.getText());
                } else {
                    downloadLink.set(input.getText());
                }
                input.clear();
                server.addPlugin(pluginName.get(), pluginType.get(), downloadLink.get());
                Arrays.sort(server.plugins, new SortByName());
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
            Arrays.sort(server.plugins, new SortByName());
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

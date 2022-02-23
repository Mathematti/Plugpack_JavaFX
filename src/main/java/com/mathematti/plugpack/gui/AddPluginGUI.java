package com.mathematti.plugpack.gui;

import com.mathematti.plugpack.functions.Server;
import com.mathematti.plugpack.functions.plugin.SortByName;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AddPluginGUI {
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
                ChoosePluginGUI.choosePluginGUI(stage, server);
            }
            question.getAndIncrement();
        });

        vBox.getChildren().addAll(inputLabel, input, confirm);
    }
}
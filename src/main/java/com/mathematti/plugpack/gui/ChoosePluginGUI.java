package com.mathematti.plugpack.gui;

import com.mathematti.plugpack.functions.Server;
import com.mathematti.plugpack.functions.plugin.Plugin;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ChoosePluginGUI {
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
        cancel.setOnAction(actionEvent -> ModifyServerGUI.modifyServerGUI(stage, server.getName()));
        Button addPlugin = new Button("Add plugin");
        addPlugin.setOnAction(actionEvent -> AddPluginGUI.addPluginGUI(stage, server));

        List<Hyperlink> hyperlinkList = new ArrayList<>();

        for (Plugin plugin : server.plugins) {
            hyperlinkList.add(new Hyperlink(plugin.getName()));
        }

        for (int i = 0; i < hyperlinkList.size(); i++) {
            int finalI = i;
            hyperlinkList.get(i).setOnAction(actionEvent -> ModifyPluginsGUI.modifyPluginGUI(stage, server, server.plugins[finalI]));
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
                } catch (Exception ignored) {
                }
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
}
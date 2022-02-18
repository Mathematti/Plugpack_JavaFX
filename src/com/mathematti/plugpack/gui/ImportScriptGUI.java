package com.mathematti.plugpack.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ImportScriptGUI {
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
        cancel.setOnAction(actionEvent -> MainMenuGUI.showMainGUI(stage));
        Button importScript = new Button("Import");
        importScript.setOnAction(actionEvent -> {
            com.mathematti.plugpack.functions.ImportScript.importScript(script);
            MainMenuGUI.showMainGUI(stage);
        });

        HBox hBox = new HBox();
        hBox.setSpacing(padding);

        hBox.getChildren().addAll(cancel, importScript);
        vBox.getChildren().addAll(output, script, hBox);
    }
}

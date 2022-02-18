package com.mathematti.plugpack.gui;

import com.mathematti.plugpack.functions.GenerateScript;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GenerateScriptGUI {
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
        cancel.setOnAction(actionEvent -> MainMenuGUI.showMainGUI(stage));

        vBox.getChildren().addAll(output, script, cancel);

        script.setText(GenerateScript.generateScript());
    }
}

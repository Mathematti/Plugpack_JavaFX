package com.mathematti.plugpack;

import com.mathematti.plugpack.gui.MainMenuGUI;
import javafx.application.Application;

/**
 * Main class for Plugpack
 * @author Mathematti
 */
public class Plugpack {
    /**
     * Main method, currently only launches the main menu
     * @param args args
     */
    public static void main(String[] args) {
        Application.launch(MainMenuGUI.class, args);
    }
}

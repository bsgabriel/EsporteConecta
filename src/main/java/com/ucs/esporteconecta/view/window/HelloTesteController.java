package com.ucs.esporteconecta.view.window;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloTesteController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

}
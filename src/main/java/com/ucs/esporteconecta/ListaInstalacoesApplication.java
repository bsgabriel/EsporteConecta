package com.ucs.esporteconecta;

import com.ucs.esporteconecta.util.FXUtils;
import com.ucs.esporteconecta.view.window.ListaInstalacoesReservarController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ListaInstalacoesApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = FXUtils.loadWindow(ListaInstalacoesReservarController.class);
        stage.setTitle("Instalações Esportivas");
        stage.setMinWidth(1400);
        stage.setMinHeight(935);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

package com.ucs.esporteconecta;

import com.ucs.esporteconecta.view.ViewResourceHelper;
import com.ucs.esporteconecta.view.window.ListaInstalacoesController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class ListaInstalacoesApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ViewResourceHelper.getWindowFxml(ListaInstalacoesController.class));

        double width = Screen.getPrimary().getVisualBounds().getWidth();
        double height = Screen.getPrimary().getVisualBounds().getHeight();

        Scene scene = new Scene(fxmlLoader.load(), width, height);
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

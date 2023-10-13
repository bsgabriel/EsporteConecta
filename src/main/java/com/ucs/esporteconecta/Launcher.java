package com.ucs.esporteconecta;

import com.ucs.esporteconecta.util.FXUtils;
import com.ucs.esporteconecta.util.GlobalData;
import com.ucs.esporteconecta.view.window.MenuInicialController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        GlobalData.setPrimaryStage(stage);
        Scene scene = FXUtils.loadWindow(MenuInicialController.class).getScene();
        stage.setTitle("Início");
        stage.setMinWidth(1400);
        stage.setMinHeight(935);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

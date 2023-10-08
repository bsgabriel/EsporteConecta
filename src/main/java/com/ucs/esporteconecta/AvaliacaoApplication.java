package com.ucs.esporteconecta;

import com.ucs.esporteconecta.view.ViewResourceHelper;
import com.ucs.esporteconecta.view.window.AvaliacaoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AvaliacaoApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ViewResourceHelper.getWindowFxml(AvaliacaoController.class));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Avaliação");
        stage.setMinWidth(1400);
        stage.setMinHeight(935);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

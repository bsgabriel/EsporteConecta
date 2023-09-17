package com.ucs.esporteconecta;

import com.ucs.esporteconecta.view.ViewResourceHelper;
import com.ucs.esporteconecta.view.window.InstalacaoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InstalacaoApplication extends Application{

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(ViewResourceHelper.getWindowFxml(InstalacaoController.class));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cadastrar instalação");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

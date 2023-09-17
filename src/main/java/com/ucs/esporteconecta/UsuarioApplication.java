package com.ucs.esporteconecta;

import com.ucs.esporteconecta.view.ViewResourceHelper;
import com.ucs.esporteconecta.view.window.UsuarioController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UsuarioApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(ViewResourceHelper.getWindowFxml(UsuarioController.class));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cadastro de Usuário");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

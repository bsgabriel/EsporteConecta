package com.ucs.esporteconecta.util;

import com.ucs.esporteconecta.util.beans.JFXLoaderBean;
import com.ucs.esporteconecta.util.interfaces.IController;
import com.ucs.esporteconecta.view.ViewResourceHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;

import java.io.IOException;

/**
 * Classe com utilitários para o JavaFX
 *
 * @author Gabriel Silveira
 */
public class FXUtils {

    public static <T extends IController> JFXLoaderBean<T> loadWindow(Class<T> controller) throws IOException {
        return loadWindow(controller, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
    }

    public static <T extends IController> JFXLoaderBean<T>  loadWindow(Class<T> controller, double width, double height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ViewResourceHelper.getWindowFxml(controller));
        Parent parent = fxmlLoader.load();
        ((IController) fxmlLoader.getController()).setRoot(parent);

        JFXLoaderBean<T> bean = new JFXLoaderBean<>();
        bean.setScene(new Scene(parent, width, height));
        bean.setControlller(fxmlLoader.getController());
        System.out.println("Tela a ser exibida: " + controller.getSimpleName());
        return bean;
    }

    /**
     * Roda eventos na thread do JavaFX
     *
     * @param run ação para executar
     */
    public static void runLater(Runnable run) {
        if (!Platform.isFxApplicationThread())
            Platform.runLater(run);
        else
            run.run();
    }
}

package com.ucs.esporteconecta.util;

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

    public static <T extends IController> Scene loadWindow(Class<T> controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ViewResourceHelper.getWindowFxml(controller));
        double width = Screen.getPrimary().getVisualBounds().getWidth();
        double height = Screen.getPrimary().getVisualBounds().getHeight();
        Parent parent = fxmlLoader.load();
        ((IController) fxmlLoader.getController()).setRoot(parent);
        return new Scene(parent, width, height);
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

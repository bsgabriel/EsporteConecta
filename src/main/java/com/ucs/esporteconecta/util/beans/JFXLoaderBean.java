package com.ucs.esporteconecta.util.beans;

import com.ucs.esporteconecta.util.interfaces.IController;
import javafx.scene.Scene;

public class JFXLoaderBean<T extends IController> {

    private T controlller;
    private Scene scene;

    public T getControlller() {
        return controlller;
    }

    public void setControlller(T controlller) {
        this.controlller = controlller;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}

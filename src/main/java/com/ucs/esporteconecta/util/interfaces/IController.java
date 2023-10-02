package com.ucs.esporteconecta.util.interfaces;

import javafx.scene.Parent;

public interface IController {
    /**
     * Passa o {@linkplain Parent} de uma tela ao seu controller
     *
     * @param root componente "pai" da tela
     */
    void setRoot(Parent root);
}

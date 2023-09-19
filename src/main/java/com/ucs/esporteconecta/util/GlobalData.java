package com.ucs.esporteconecta.util;

import com.ucs.esporteconecta.model.Usuario;
import javafx.stage.Stage;

public class GlobalData {

    private static Usuario usuarioLogado;
    private static Stage primaryStage;

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuarioLogado) {
        GlobalData.usuarioLogado = usuarioLogado;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        GlobalData.primaryStage = primaryStage;
    }
}

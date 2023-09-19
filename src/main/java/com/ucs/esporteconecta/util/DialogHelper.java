package com.ucs.esporteconecta.util;

import javafx.geometry.Pos;
import org.controlsfx.control.Notifications;

public class DialogHelper {

    public static void showErrorDialog(String text) {
        createDialog() //
                .text(text) //
                .title("Erro") //
                .showError();
    }

    public static void showWarning(String text) {
        createDialog() //
                .text(text) //
                .title("Alerta") //
                .showWarning();
    }

    public static void showInformation(String text) {
        createDialog() //
                .text(text) //
                .title("Aviso") //
                .showInformation();
    }

    private static Notifications createDialog() {
        return Notifications.create() //
                .position(Pos.CENTER)
                .owner(GlobalData.getPrimaryStage());
    }

}

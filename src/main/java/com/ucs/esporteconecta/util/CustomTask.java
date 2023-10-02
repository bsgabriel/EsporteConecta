package com.ucs.esporteconecta.util;

import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.Parent;

public abstract class CustomTask<V> extends Task<V> {
    private Parent root;

    /**
     * Código a ser executado quando a Task terminar a execução do método {@linkplain CustomTask#call()}
     *
     * @param value valor retornado pela task
     */
    protected abstract void sucesso(V value);


    /**
     * Código a ser executado caso houver algum erro durante a execução do método {@linkplain CustomTask#call()}
     *
     * @param throwable erro
     */
    protected abstract void erro(Throwable throwable);

    public void executar(Parent root) {
        this.root = root;
        bloquear(true);
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    private void bloquear(final boolean bloquear) {
        if (root != null) {
            FXUtils.runLater(() -> root.setDisable(bloquear));
            if (bloquear)
                root.setCursor(Cursor.WAIT);
            else
                root.setCursor(Cursor.DEFAULT);
        }
    }

    @Override
    protected void succeeded() {
        try {
            sucesso(get());
        } catch (Exception e) {
            DialogHelper.showErrorDialog("Falha ao executar task");
            e.printStackTrace();
        } finally {
            bloquear(false);
            super.succeeded();
        }
    }

    @Override
    protected void failed() {
        try {
            erro(this.exceptionProperty().get());
        } finally {
            bloquear(false);
            super.failed();
        }
    }
}

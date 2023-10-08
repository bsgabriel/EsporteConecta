package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.util.FXUtils;
import com.ucs.esporteconecta.util.GlobalData;
import com.ucs.esporteconecta.util.interfaces.IController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

import java.io.IOException;

public class ListaInstalacoesEditarController implements IController {

    @FXML
    private Button btnCadastroInst;

    @FXML
    private Button btnSair;

    @FXML
    private ScrollPane spnInstalacoes;

    @FXML
    private ScrollPane spnProxReservas;

    private Parent parent;

    @Override
    public void setRoot(Parent root) {
        this.parent = root;
    }

    @FXML
    void onClickCadastroInst(ActionEvent event) {

        try {
            Scene scene = null;
            scene = FXUtils.loadWindow(InstalacaoController.class);
            GlobalData.getPrimaryStage().setTitle("Cadastrar instalação");
            GlobalData.getPrimaryStage().setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onClickSair(ActionEvent event) {

        try {
            Scene scene = null;
            scene = FXUtils.loadWindow(MenuInicialController.class);
            GlobalData.getPrimaryStage().setTitle("Início");
            GlobalData.getPrimaryStage().setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

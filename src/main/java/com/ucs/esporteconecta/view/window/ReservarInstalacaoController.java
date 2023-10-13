package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.model.Funcionamento;
import com.ucs.esporteconecta.model.Instalacao;
import com.ucs.esporteconecta.util.interfaces.IController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Date;

public class ReservarInstalacaoController implements IController {
    private Parent root;

    @FXML
    private Label lblNomeInstalacao;

    @FXML
    private DatePicker dtPicker;

    @FXML
    private ComboBox<Funcionamento> cbFuncionamento;

    private Instalacao instalacao;

    public Instalacao getInstalacao() {
        return instalacao;
    }

    public void setInstalacao(Instalacao instalacao) {
        this.instalacao = instalacao;

        if (instalacao != null) {
            lblNomeInstalacao.setText(instalacao.getNome());
            cbFuncionamento.getItems().addAll(instalacao.getFuncionamentos());
        }
    }

    @Override
    public void setRoot(Parent root) {
        this.root = root;
    }

    @FXML
    private void initialize() {

    }

    @FXML
    private void actReservar() {
        // TODO: verificar se tem data válida, e se tem funcionamento selecionado
    }

    @FXML
    private void actCancelar() {
        ((Stage) this.root.getScene().getWindow()).close();
    }

}

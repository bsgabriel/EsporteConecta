package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.database.dao.InstalacaoDAO;
import com.ucs.esporteconecta.database.dao.UsuarioDAO;
import com.ucs.esporteconecta.model.Instalacao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import static com.ucs.esporteconecta.util.DialogHelper.showErrorDialog;
import static com.ucs.esporteconecta.util.DialogHelper.showInformation;

public class InstalacaoController {

    @FXML
    private GridPane PnlEndereco;

    @FXML
    private Button btnSalvar;

    @FXML
    private TextField inputBairro;

    @FXML
    private TextField inputCapacidadeMax;

    @FXML
    private TextField inputCidade;

    @FXML
    private TextField inputDesc;

    @FXML
    private TextField inputEstado;

    @FXML
    private TextField inputHoraFim;

    @FXML
    private TextField inputHoraInicio;

    @FXML
    private TextField inputNome;

    @FXML
    private TextField inputValor;

    @FXML
    private Label lblData;

    @FXML
    private Label lblSubtitulo;

    @FXML
    private GridPane pnlGridHorario;

    @FXML
    private AnchorPane pnlMain;

    @FXML
    private GridPane pnlMainGrid;

    @FXML
    private ChoiceBox<?> selectDiaFim;

    @FXML
    private ChoiceBox<?> selectDiaInicio;

    @FXML
    private ChoiceBox<?> selectModalidade;

    @FXML
    private Label title;

    private InstalacaoDAO instalacaoDAO;

    private InstalacaoDAO getInstalacaoDAO() {
        if (instalacaoDAO == null)
            instalacaoDAO = new InstalacaoDAO();
        return instalacaoDAO;
    }

    @FXML
    void onClickBtnSalvar(ActionEvent event) {

        //Validar campos

        Instalacao instalacao = new Instalacao();
        instalacao.setNome(inputNome.getText());
        instalacao.setDescricao(inputDesc.getText());
        instalacao.setBairro(inputBairro.getText());
        instalacao.setCidade(inputCidade.getText());
        instalacao.setEstado(inputEstado.getText());

        //Validar se valor informado é numerico
        instalacao.setValor(Double.parseDouble(inputValor.getText()));
        instalacao.setCapacidadeMaxima(Integer.parseInt(inputValor.getText()));

        //Salvar horario de funcionamento

        if (!getInstalacaoDAO().persist(instalacao)) {
            showErrorDialog("Não foi possível realizar cadastro");
            return;
        }

        showInformation("Instalacao cadastrada com sucesso!");

    }

}

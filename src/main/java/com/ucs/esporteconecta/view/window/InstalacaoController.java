package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.database.dao.InstalacaoDAO;
import com.ucs.esporteconecta.database.dao.ModalidadeDAO;
import com.ucs.esporteconecta.model.*;
import com.ucs.esporteconecta.model.enums.DiaSemana;
import com.ucs.esporteconecta.util.MascarasFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static com.ucs.esporteconecta.util.DialogHelper.*;
import static com.ucs.esporteconecta.util.DialogHelper.showWarning;

public class InstalacaoController implements Initializable {

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
    private TextField inputHoraFimManha;

    @FXML
    private TextField inputHoraFimTarde;

    @FXML
    private TextField inputHoraInicioManha;

    @FXML
    private TextField inputHoraInicioTarde;

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
    private ChoiceBox<DiaSemana> selectDiaFim;

    @FXML
    private ChoiceBox<DiaSemana> selectDiaInicio;

    @FXML
    private ChoiceBox<Modalidade> selectModalidade;

    @FXML
    private Label title;

    private InstalacaoDAO instalacaoDAO;
    private ModalidadeDAO modalidadeDAO;

    private InstalacaoDAO getInstalacaoDAO() {
        if (instalacaoDAO == null)
            instalacaoDAO = new InstalacaoDAO();
        return instalacaoDAO;
    }

    private ModalidadeDAO getModalidadeDAO() {
        if (modalidadeDAO == null)
            modalidadeDAO = new ModalidadeDAO();
        return modalidadeDAO;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        List<Modalidade> modalidades = getModalidadeDAO().findAll();
        selectModalidade.getItems().addAll(modalidades);

        List<DiaSemana> diasDaSemana = Arrays.asList(DiaSemana.values());
        selectDiaInicio.getItems().addAll(diasDaSemana);
        selectDiaFim.getItems().addAll(diasDaSemana);

    }

    @FXML
    void onClickBtnSalvar(ActionEvent event) {

        if (!this.validarCampos()){
            return;
        }

        Instalacao instalacao = new Instalacao();
        instalacao.setNome(inputNome.getText());
        instalacao.setDescricao(inputDesc.getText());
        instalacao.setBairro(inputBairro.getText());
        instalacao.setCidade(inputCidade.getText());
        instalacao.setEstado(inputEstado.getText());

        instalacao.setValor(Double.parseDouble(inputValor.getText().replaceAll(",", ".")));
        instalacao.setCapacidadeMaxima(Integer.parseInt(inputCapacidadeMax.getText()));

        //Salvar modalidade
        String[] idModalidade = this.selectModalidade.getValue().toString().split("-");
        Modalidade modalidade = modalidadeDAO.buscarPorId(Integer.parseInt(idModalidade[0]));
        instalacao.setModalidade(modalidade);

        //Salvar horario de funcionamento
        Funcionamento func1 = cadastraHorarioFuncionamento(this.inputHoraInicioManha, this.inputHoraFimManha, this.selectDiaInicio);
        Funcionamento func2 = cadastraHorarioFuncionamento(this.inputHoraInicioTarde, this.inputHoraFimTarde, this.selectDiaFim);

         func1.setInstalacao(instalacao);
         func2.setInstalacao(instalacao);

         instalacao.getFuncionamentos().add(func1);
         instalacao.getFuncionamentos().add(func2);

        //Salva o id da instituicao

        if (!getInstalacaoDAO().persist(instalacao)) {
            showErrorDialog("Não foi possível realizar cadastro");
            return;
        }

        showInformation("Instalacao cadastrada com sucesso!");

    }

    @FXML
    void onKeyPressedHrFimManha(KeyEvent event) {
        MascarasFX.mascaraHora(this.inputHoraFimManha);
    }

    @FXML
    void onKeyPressedHrFimTarde(KeyEvent event) {
        MascarasFX.mascaraHora(this.inputHoraFimTarde);
    }

    @FXML
    void onKeyPressedHrInicioManha(KeyEvent event) {
        MascarasFX.mascaraHora(this.inputHoraInicioManha);
    }

    @FXML
    void onKeyPressedHrInicioTarde(KeyEvent event) {
        MascarasFX.mascaraHora(this.inputHoraInicioTarde);
    }

    private Funcionamento cadastraHorarioFuncionamento(TextField inptHoraInicio, TextField inptHoraFim, ChoiceBox<DiaSemana> diaSemana) {
        String[] horaInicio = inptHoraInicio.getText().split(":");
        String[] horaFim = inptHoraFim.getText().split(":");

        Horario horario = new Horario();
        horario.setInicio(LocalTime.of(Integer.parseInt(horaInicio[0]), Integer.parseInt(horaInicio[1])));
        horario.setFim(LocalTime.of(Integer.parseInt(horaFim[0]), Integer.parseInt(horaFim[1])));

        Funcionamento func = new Funcionamento();
        func.setDiaSemana(diaSemana.getValue());
        func.setHorario(horario);
        return func;
    }

    private boolean validarCampos() {
        if (inputNome.getText() == null || inputNome.getText().isBlank()) {
            showWarning("Nome não informado");
            return false;
        }

        if (inputDesc.getText() == null || inputDesc.getText().isBlank()) {
            showWarning("Descricao não informada");
            return false;
        }

        if (inputBairro.getText() == null || inputBairro.getText().isBlank()) {
            showWarning("Bairro não informado");
            return false;
        }

        if (inputCidade.getText() == null || inputCidade.getText().isBlank()) {
            showWarning("Cidade não informada");
            return false;
        }

        if (inputEstado.getText() == null || inputEstado.getText().isBlank()) {
            showWarning("Estado não informado");
            return false;
        }

        if (inputValor.getText() == null || inputValor.getText().isBlank()) {
            showWarning("Valor da mensalidade não informado");
            return false;
        }

        if (inputCapacidadeMax.getText() == null || inputCapacidadeMax.getText().isBlank()) {
            showWarning("Capacidade máxima não informada");
            return false;
        }

        if (inputHoraInicioManha.getText() == null || inputHoraInicioManha.getText().isBlank()) {
            showWarning("Horario de abertura da manhã não informada");
            return false;
        }

        if (inputHoraFimManha.getText() == null || inputHoraFimManha.getText().isBlank()) {
            showWarning("Horario de fechamento da manhã não informada");
            return false;
        }

        if (inputHoraInicioTarde.getText() == null || inputHoraInicioTarde.getText().isBlank()) {
            showWarning("Horario de abertura da tarde não informada");
            return false;
        }

        if (inputHoraFimTarde.getText() == null || inputHoraFimTarde.getText().isBlank()) {
            showWarning("Horario de fechamento da tarde não informada");
            return false;
        }

        if (selectModalidade.getValue() == null || selectModalidade.getValue().toString().isBlank()) {
            showWarning("Modalidade não informada");
            return false;
        }

        if (selectDiaInicio.getValue() == null || selectDiaInicio.getValue().toString().isBlank()) {
            showWarning("Dia de abertura não informada");
            return false;
        }

        if (selectDiaFim.getValue() == null || selectDiaFim.getValue().toString().isBlank()) {
            showWarning("Dia de fechamento não informada");
            return false;
        }

        //Validacao campos numericos

        try {
            Double.parseDouble(inputValor.getText().replaceAll(",", "."));
        } catch (Exception e){
            showWarning("Digite apenas números para informar o valor da mensalidade");
            return false;
        }

        try {
            Integer.parseInt(inputCapacidadeMax.getText());
        } catch (Exception e){
            showWarning("Digite apenas números para informar a capacidade máxima");
            return false;
        }

        return true;
    }

}

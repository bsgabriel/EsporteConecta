package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.database.dao.InstalacaoDAO;
import com.ucs.esporteconecta.database.dao.ModalidadeDAO;
import com.ucs.esporteconecta.model.*;
import com.ucs.esporteconecta.model.enums.DiaSemana;
import com.ucs.esporteconecta.util.FXUtils;
import com.ucs.esporteconecta.util.GlobalData;
import com.ucs.esporteconecta.util.MascarasFX;
import com.ucs.esporteconecta.util.interfaces.IController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static com.ucs.esporteconecta.util.DialogHelper.*;
import static com.ucs.esporteconecta.util.DialogHelper.showWarning;

public class InstalacaoController implements IController, Initializable {

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
    private ChoiceBox<DiaSemana> selectDiaFim;

    @FXML
    private ChoiceBox<DiaSemana> selectDiaInicio;

    @FXML
    private ChoiceBox<Modalidade> selectModalidade;

    private Instalacao inst;

    private boolean editar = false;
    private int id; //Id da instalacao caso esteja em edicao

    private Parent parent;

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
    public void setRoot(Parent root) {
        this.parent = root;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        this.inst = new Instalacao();

        List<Modalidade> modalidades = getModalidadeDAO().findAll();
        selectModalidade.getItems().addAll(modalidades);

        List<DiaSemana> diasDaSemana = Arrays.asList(DiaSemana.values());
        selectDiaInicio.getItems().addAll(diasDaSemana);
        selectDiaFim.getItems().addAll(diasDaSemana);

    }

    public void setId(int id) {
        this.id = id;
        editar = true;
        carregarDados();
    }

    public void carregarDados() {
        this.inst = getInstalacaoDAO().findOne(this.id);

        this.inputNome.setText(inst.getNome());
        this.inputDesc.setText(inst.getDescricao());
        this.inputEstado.setText(inst.getEstado());
        this.inputCidade.setText(inst.getCidade());
        this.inputBairro.setText(inst.getBairro());
        this.inputValor.setText(inst.getValor().toString());
        this.selectModalidade.setValue(inst.getModalidade());
        this.inputCapacidadeMax.setText(inst.getCapacidadeMaxima().toString());
        this.selectDiaInicio.setValue(inst.getFuncionamentos().get(0).getDiaSemana());
        this.selectDiaFim.setValue(inst.getFuncionamentos().get(1).getDiaSemana());
        this.inputHoraInicio.setText(inst.getFuncionamentos().get(0).getHorario().getInicio().toString());
        this.inputHoraFim.setText(inst.getFuncionamentos().get(0).getHorario().getFim().toString());

    }

    @FXML
    void onClickBtnSalvar(ActionEvent event) {

        if (!this.validarCampos()){
            return;
        }

        this.inst.setNome(inputNome.getText());
        this.inst.setDescricao(inputDesc.getText());
        this.inst.setBairro(inputBairro.getText());
        this.inst.setCidade(inputCidade.getText());
        this.inst.setEstado(inputEstado.getText());

        this.inst.setValor(Double.parseDouble(inputValor.getText().replaceAll(",", ".")));
        this.inst.setCapacidadeMaxima(Integer.parseInt(inputCapacidadeMax.getText()));

        //Salvar modalidade
        String[] idModalidade = this.selectModalidade.getValue().toString().split("-");
        Modalidade modalidade = modalidadeDAO.buscarPorId(Integer.parseInt(idModalidade[0]));
        this.inst.setModalidade(modalidade);

        if (editar) {
            alteraHorarioFuncionamento(this.inputHoraInicio, this.inputHoraFim, this.selectDiaInicio, this.inst.getFuncionamentos().get(0).getHorario(), this.inst.getFuncionamentos().get(0));
            alteraHorarioFuncionamento(this.inputHoraInicio, this.inputHoraFim, this.selectDiaFim, this.inst.getFuncionamentos().get(1).getHorario(), this.inst.getFuncionamentos().get(1));
        } else {

            //Salvar horario de funcionamento
            Funcionamento func1 = cadastraHorarioFuncionamento(this.inputHoraInicio, this.inputHoraFim, this.selectDiaInicio);
            func1.setInstalacao(this.inst);
            this.inst.getFuncionamentos().add(func1);

            Funcionamento func2 = cadastraHorarioFuncionamento(this.inputHoraInicio, this.inputHoraFim, this.selectDiaFim);
            func2.setInstalacao(this.inst);
            this.inst.getFuncionamentos().add(func2);

        }

        //Salva o id da instituicao
        if (GlobalData.getUsuarioLogado() instanceof Instituicao) {
            Usuario user = GlobalData.getUsuarioLogado();
            this.inst.setInstituicao((Instituicao) user);
        }

        if (this.editar) {

            if (!getInstalacaoDAO().update(this.inst)) {
                showErrorDialog("Não foi possível atualizar o cadastro");
                return;
            }

        } else {

            if (!getInstalacaoDAO().persist(this.inst)) {
                showErrorDialog("Não foi possível realizar cadastro");
                return;
            }

        }

        showInformation("Instalacao salva com sucesso!");

    }

    @FXML
    void onClickVoltar(ActionEvent event) {

        try {
            Scene scene = null;
            scene = FXUtils.loadWindow(ListaInstalacoesEditarController.class).getScene();
            GlobalData.getPrimaryStage().setTitle("Minhas instalações");
            GlobalData.getPrimaryStage().setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onKeyPressedHrFim(KeyEvent event) {
        MascarasFX.mascaraHora(this.inputHoraFim);
    }

    @FXML
    void onKeyPressedHrInicio(KeyEvent event) {
        MascarasFX.mascaraHora(this.inputHoraInicio);
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

    private void alteraHorarioFuncionamento(TextField inptHoraInicio, TextField inptHoraFim, ChoiceBox<DiaSemana> diaSemana, Horario horario, Funcionamento func) {
        String[] horaInicio = inptHoraInicio.getText().split(":");
        String[] horaFim = inptHoraFim.getText().split(":");

        horario.setInicio(LocalTime.of(Integer.parseInt(horaInicio[0]), Integer.parseInt(horaInicio[1])));
        horario.setFim(LocalTime.of(Integer.parseInt(horaFim[0]), Integer.parseInt(horaFim[1])));

        func.setDiaSemana(diaSemana.getValue());
        func.setHorario(horario);
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

        if (inputHoraInicio.getText() == null || inputHoraInicio.getText().isBlank()) {
            showWarning("Horario de abertura da manhã não informada");
            return false;
        }

        if (inputHoraFim.getText() == null || inputHoraFim.getText().isBlank()) {
            showWarning("Horario de fechamento da manhã não informada");
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

package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.database.dao.ReservaDAO;
import com.ucs.esporteconecta.model.Esportista;
import com.ucs.esporteconecta.model.Funcionamento;
import com.ucs.esporteconecta.model.Instalacao;
import com.ucs.esporteconecta.model.Reserva;
import com.ucs.esporteconecta.model.enums.DiaSemana;
import com.ucs.esporteconecta.util.CustomTask;
import com.ucs.esporteconecta.util.DialogHelper;
import com.ucs.esporteconecta.util.GlobalData;
import com.ucs.esporteconecta.util.exceptions.ReservaExistenteException;
import com.ucs.esporteconecta.util.interfaces.IController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.util.List;

public class ReservarInstalacaoController implements IController {
    private Parent root;

    @FXML
    private Label lblNomeInstalacao;

    @FXML
    private DatePicker dtPicker;

    @FXML
    private ComboBox<Funcionamento> cbFuncionamento;

    @FXML
    private Label lblMensagem;

    private Instalacao instalacao;
    private ReservaDAO reservaDAO;

    private ReservaDAO getReservaDAO() {
        if (reservaDAO == null)
            reservaDAO = new ReservaDAO();

        return reservaDAO;
    }

    public Instalacao getInstalacao() {
        return instalacao;
    }

    public void setInstalacao(Instalacao instalacao) {
        this.instalacao = instalacao;

        if (instalacao != null) {
            lblNomeInstalacao.setText(instalacao.getNome());
        }
    }

    @Override
    public void setRoot(Parent root) {
        this.root = root;
    }

    @FXML
    private void initialize() {
        lblMensagem.textProperty().addListener((observable, oldText, newText) -> {
            lblMensagem.managedProperty().set(newText != null && !newText.isBlank());
            lblMensagem.visibleProperty().set(newText != null && !newText.isBlank());
        });

        dtPicker.valueProperty().addListener((observable, oldVal, newVal) -> {
            cbFuncionamento.getItems().clear();
            DiaSemana diaSemanaSelecionado = traduzirDiaSemana(newVal.getDayOfWeek());
            List<Funcionamento> filtrado = instalacao.getFuncionamentos().stream().filter(funcionamento -> funcionamento.getDiaSemana().equals(diaSemanaSelecionado)).toList();

            if (filtrado.isEmpty()) {
                lblMensagem.setText("Nenhum funcionamento cadastrado para " + diaSemanaSelecionado);
                cbFuncionamento.setDisable(true);
            } else {
                cbFuncionamento.setDisable(false);
                lblMensagem.setText("Selecione um horário para " + diaSemanaSelecionado);
                cbFuncionamento.getItems().addAll(filtrado);
            }
        });
    }

    @FXML
    private void actReservar() {
        if (!validarSelecao()) {
            return;
        }

        new CustomTask<Void>() {
            @Override
            protected Void call() throws Exception {
                Reserva reserva = getReservaDAO().buscar(instalacao, cbFuncionamento.getSelectionModel().getSelectedItem().getHorario(), dtPicker.getValue());
                if (reserva != null)
                    throw new ReservaExistenteException("Já existe uma reserva para essa data e horário");

                reserva = new Reserva();
                reserva.setInstalacao(instalacao);
                reserva.setEsportista((Esportista) GlobalData.getUsuarioLogado());
                reserva.setData(dtPicker.getValue());
                reserva.setHorario(cbFuncionamento.getSelectionModel().getSelectedItem().getHorario());
                getReservaDAO().persist(reserva);

                return null;
            }

            @Override
            protected void sucesso(Void value) {
                DialogHelper.showInformation("Reserva realizada com sucesso!");
                close();
            }

            @Override
            protected void erro(Throwable throwable) {
                DialogHelper.showErrorDialog(throwable.getMessage());
            }
        }.executar(root);
    }

    @FXML
    private void actCancelar() {
        close();
    }

    private boolean validarSelecao() {
        if (dtPicker.getValue() == null) {
            DialogHelper.showErrorDialog("Selecione uma data");
            return false;
        }

        if (cbFuncionamento.getSelectionModel().getSelectedItem() == null) {
            DialogHelper.showErrorDialog("Selecione um horário");
            return false;
        }

        return true;
    }

    /**
     * Mapeia um enum {@linkplain DayOfWeek} para um {@linkplain DiaSemana}
     *
     * @param dayOfWeek dia da semana em inglês
     * @return o {@linkplain DiaSemana} correspondente
     */
    private DiaSemana traduzirDiaSemana(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case SUNDAY -> DiaSemana.DOMINGO;
            case MONDAY -> DiaSemana.SEGUNDA_FEIRA;
            case TUESDAY -> DiaSemana.TERCA_FEIRA;
            case WEDNESDAY -> DiaSemana.QUARTA_FEIRA;
            case THURSDAY -> DiaSemana.QUINTA_FEIRA;
            case FRIDAY -> DiaSemana.SEXTA_FEIRA;
            case SATURDAY -> DiaSemana.SABADO;
        };
    }

    private void close() {
        ((Stage) this.root.getScene().getWindow()).close();
    }

}

package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.database.dao.ReservaDAO;
import com.ucs.esporteconecta.model.Esportista;
import com.ucs.esporteconecta.model.Reserva;
import com.ucs.esporteconecta.util.CustomTask;
import com.ucs.esporteconecta.util.DialogHelper;
import com.ucs.esporteconecta.util.FXUtils;
import com.ucs.esporteconecta.util.GlobalData;
import com.ucs.esporteconecta.util.beans.JFXLoaderBean;
import com.ucs.esporteconecta.util.interfaces.IController;
import com.ucs.esporteconecta.view.component.ItemReservaEsportista;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListaReservasEsportista implements IController {
    private Parent root;

    @FXML
    private ScrollPane spReservas;

    @FXML
    private VBox reservasWrapper;

    private ReservaDAO reservaDAO;

    private ReservaDAO getReservaDAO() {
        if (reservaDAO == null)
            reservaDAO = new ReservaDAO();
        return reservaDAO;
    }

    @Override
    public void setRoot(Parent root) {
        this.root = root;
    }

    @FXML
    private void sair() {
        JFXLoaderBean<MenuInicialController> bean;
        try {
            bean = FXUtils.loadWindow(MenuInicialController.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GlobalData.getPrimaryStage().setTitle("Login / Cadastro");
        GlobalData.getPrimaryStage().setScene(bean.getScene());
    }

    @FXML
    private void abrirTelaReserva() {
        JFXLoaderBean<ListaInstalacoesReservarController> bean;
        try {
            bean = FXUtils.loadWindow(ListaInstalacoesReservarController.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GlobalData.getPrimaryStage().setTitle("Reservar instalação");
        GlobalData.getPrimaryStage().setScene(bean.getScene());
    }

    @FXML
    private void initialize() {
        carregarReservas();
    }

    private void carregarReservas() {
        reservasWrapper.getChildren().clear();
        new CustomTask<List<Reserva>>() {
            @Override
            protected List<Reserva> call() throws Exception {
                return getReservaDAO().buscar((Esportista) GlobalData.getUsuarioLogado());
            }

            @Override
            protected void sucesso(List<Reserva> reservas) {
                reservas.forEach(r -> adicionarReserva(r));
            }

            @Override
            protected void erro(Throwable throwable) {
                DialogHelper.showErrorDialog("Não foi possível buscar as reservas para o usuário");
            }
        }.executar(this.root);
    }

    private void adicionarReserva(Reserva r) {
        ItemReservaEsportista item = new ItemReservaEsportista();
        item.setReserva(r);
        item.setOnSave(this::carregarReservas);
        reservasWrapper.getChildren().add(item);
    }

}

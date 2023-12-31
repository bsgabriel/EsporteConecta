package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.database.dao.InstalacaoDAO;
import com.ucs.esporteconecta.database.dao.ReservaDAO;
import com.ucs.esporteconecta.model.*;
import com.ucs.esporteconecta.util.FXUtils;
import com.ucs.esporteconecta.util.GlobalData;
import com.ucs.esporteconecta.util.interfaces.IController;
import com.ucs.esporteconecta.view.ViewResourceHelper;
import com.ucs.esporteconecta.view.component.ItemReservaInstalacao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.math.RoundingMode.HALF_DOWN;

public class ListaInstalacoesEditarController implements IController, Initializable {

    @FXML
    private Button btnCadastroInst;

    @FXML
    private Button btnSair;

    @FXML
    private VBox instalacoesWrapper;

    @FXML
    private VBox proxReservasWrapper;

    @FXML
    private ScrollPane spnInstalacoes;

    @FXML
    private ScrollPane spnProxReservas;

    private Parent parent;

    private InstalacaoDAO instalacaoDAO;

    private ReservaDAO reservaDAO;

    private InstalacaoDAO getInstalacaoDAO() {
        if (instalacaoDAO == null)
            instalacaoDAO = new InstalacaoDAO();
        return instalacaoDAO;
    }

    private ReservaDAO getReservaDAO() {
        if (reservaDAO == null)
            reservaDAO = new ReservaDAO();
        return reservaDAO;
    }

    @Override
    public void setRoot(Parent root) {
        this.parent = root;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        if (GlobalData.getUsuarioLogado() instanceof Instituicao) {

            buscarProxReservas();

            buscarInstalacoes();

        }

    }

    @FXML
    void onClickCadastroInst(ActionEvent event) {

        try {
            Scene scene = null;
            scene = FXUtils.loadWindow(InstalacaoController.class).getScene();
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
            scene = FXUtils.loadWindow(MenuInicialController.class).getScene();
            GlobalData.getPrimaryStage().setTitle("Início");
            GlobalData.getPrimaryStage().setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void buscarInstalacoes() {
        this.instalacoesWrapper.getChildren().clear();

        Usuario user = GlobalData.getUsuarioLogado();

        List<Instalacao> instalacoes = getInstalacaoDAO().findByInstituicao((Instituicao) user);
        instalacoes.forEach(instalacao -> inserirInstalacao(instalacao));

    }

    private void inserirInstalacao(Instalacao instalacao) {
        ItemReservaInstalacao content = new ItemReservaInstalacao();
        content.setNome(instalacao.getNome() + " - " + instalacao.getBairro() + ", " + instalacao.getCidade());
        content.setDescricao(instalacao.getDescricao());
        content.setValorDiaria(instalacao.getValor());
        content.adicionarModalidade(instalacao.getModalidade().getNome());
        content.setLblBtnAcao("Editar");

        content.onClickBtnAcao(event -> {

            try {

                FXMLLoader fxmlLoader = new FXMLLoader(ViewResourceHelper.getWindowFxml(InstalacaoController.class));
                double width = Screen.getPrimary().getVisualBounds().getWidth();
                double height = Screen.getPrimary().getVisualBounds().getHeight();
                Parent parent = fxmlLoader.load();

                InstalacaoController controller = fxmlLoader.getController();
                controller.setId(instalacao.getId().intValue());

                ((IController) fxmlLoader.getController()).setRoot(parent);

                GlobalData.getPrimaryStage().setTitle("Editar instalacao");
                GlobalData.getPrimaryStage().setScene(new Scene(parent, width, height));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        double nota = calcularAvaliacao(instalacao.getAvaliacoes());
        content.setAvaliacao(nota);
        instalacoesWrapper.getChildren().add(content);
    }

    /**
     * Calcula a média de estrelas de uma instalação
     *
     * @param avaliacoes lista com avaliações
     * @return soma das notas divido pelo total de avaliações
     */
    private double calcularAvaliacao(List<Avaliacao> avaliacoes) {
        if (avaliacoes == null || avaliacoes.isEmpty())
            return 0.0;

        int nota = 0;

        for (Avaliacao avalicao : avaliacoes) {
            nota += avalicao.getNota();
        }

        return BigDecimal.valueOf(Integer.valueOf(nota).doubleValue() / avaliacoes.size()).setScale(1, HALF_DOWN).doubleValue();
    }

    private void buscarProxReservas() {

        Usuario user = GlobalData.getUsuarioLogado();

        List<Reserva> reservas = getReservaDAO().buscarPorInstituicao((Instituicao) user);

        List<String> lista = new ArrayList<String>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat numberFormatter = new DecimalFormat("###,###.##");

        for (Reserva reserva: reservas) {
                lista.add(reserva.getData().format(dateFormatter) + " - " + reserva.getInstalacao().getNome() + " (" + reserva.getEsportista().getNome() + ") - R$" +
                      numberFormatter.format(reserva.getInstalacao().getValor()));
        }

        ObservableList<String> items = FXCollections.observableList(lista);
        ListView<String> listaReservas = new ListView<String>(items);

        this.proxReservasWrapper.getChildren().add(listaReservas);
    }

}

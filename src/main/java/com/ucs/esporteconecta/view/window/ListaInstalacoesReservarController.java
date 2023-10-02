package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.database.dao.InstalacaoDAO;
import com.ucs.esporteconecta.model.Avaliacao;
import com.ucs.esporteconecta.model.Instalacao;
import com.ucs.esporteconecta.util.CustomTask;
import com.ucs.esporteconecta.util.DialogHelper;
import com.ucs.esporteconecta.util.interfaces.IController;
import com.ucs.esporteconecta.view.component.ItemReservaInstalacao;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;

import java.math.BigDecimal;
import java.util.List;

import static java.math.RoundingMode.HALF_DOWN;

public class ListaInstalacoesReservarController implements IController {

    @FXML
    private ComboBox cbModalidade;

    @FXML
    private DatePicker dpPeriodo;

    @FXML
    private TextField fldEstado;

    @FXML
    private TextField fldCidade;

    @FXML
    private TextField fldBairro;

    @FXML
    private TextField fldValor;

    @FXML
    private Rating rating;

    @FXML
    private Button btnBuscar;

    @FXML
    private ScrollPane spInstalacoes;

    @FXML
    private VBox instalacoesWrapper;

    private Parent parent;
    private InstalacaoDAO instalacaoDAO;

    private InstalacaoDAO getInstalacaoDAO() {
        if (instalacaoDAO == null)
            instalacaoDAO = new InstalacaoDAO();
        return instalacaoDAO;
    }

    @Override
    public void setRoot(Parent root) {
        this.parent = root;
    }

    @FXML
    private void buscarInstalacoes() {
        this.instalacoesWrapper.getChildren().clear();
        new CustomTask<List<Instalacao>>() {
            @Override
            protected List<Instalacao> call() {
                return getInstalacaoDAO().findAll();
            }

            @Override
            protected void sucesso(List<Instalacao> instalacoes) {
                instalacoes.forEach(instalacao -> inserirInstalacao(instalacao));
            }

            @Override
            protected void erro(Throwable throwable) {
                DialogHelper.showErrorDialog("Não foi possível buscar instalações");
                throwable.printStackTrace();
            }
        }.executar(parent);
    }

    private void inserirInstalacao(Instalacao instalacao) {
        ItemReservaInstalacao content = new ItemReservaInstalacao();
        content.setNome(instalacao.getNome() + " - " + instalacao.getBairro() + ", " + instalacao.getCidade());
        content.setDescricao(instalacao.getDescricao());
        content.setValorDiaria(instalacao.getValor());
        content.adicionarModalidade(instalacao.getModalidade().getNome());

        double nota = calcularAvaliacao(instalacao.getAvaliacoes());
        content.setAvaliacao(nota);
        instalacoesWrapper.getChildren().add(content);
    }


    private double calcularAvaliacao(List<Avaliacao> avaliacoes) {
        if (avaliacoes == null || avaliacoes.isEmpty())
            return 0.0;

        double nota = 0.0;

        for (Avaliacao avalicao : avaliacoes) {
            nota += avalicao.getNota();
        }

        System.out.println("nota total: " + nota);
        return BigDecimal.valueOf(nota / avaliacoes.size()).setScale(1, HALF_DOWN).doubleValue();
    }

}

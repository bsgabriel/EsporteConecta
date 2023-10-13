package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.database.dao.InstalacaoDAO;
import com.ucs.esporteconecta.model.Avaliacao;
import com.ucs.esporteconecta.model.Instalacao;
import com.ucs.esporteconecta.model.Modalidade;
import com.ucs.esporteconecta.util.CustomTask;
import com.ucs.esporteconecta.util.DialogHelper;
import com.ucs.esporteconecta.util.FXUtils;
import com.ucs.esporteconecta.util.beans.JFXLoaderBean;
import com.ucs.esporteconecta.util.filtros.FiltroBuscaInstalacao;
import com.ucs.esporteconecta.util.interfaces.IController;
import com.ucs.esporteconecta.view.component.ItemReservaInstalacao;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static com.ucs.esporteconecta.util.GlobalData.getPrimaryStage;
import static java.math.RoundingMode.HALF_DOWN;

public class ListaInstalacoesReservarController implements IController {

    @FXML
    private ComboBox<Modalidade> cbModalidade;

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
    private void limparFiltro() {
        cbModalidade.getSelectionModel().clearSelection();
        cbModalidade.setPromptText("Modalidade");
        dpPeriodo.setValue(null);
        fldEstado.setText("");
        fldCidade.setText("");
        fldBairro.setText("");
        fldValor.setText("");
        rating.setRating(0);
    }

    @FXML
    private void buscarInstalacoes() {
        final FiltroBuscaInstalacao filtro = gerarFiltro();
        this.instalacoesWrapper.getChildren().clear();
        new CustomTask<List<Instalacao>>() {
            @Override
            protected List<Instalacao> call() {
                if (filtro != null)
                    return getInstalacaoDAO().find(filtro);
                else
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

    private FiltroBuscaInstalacao gerarFiltro() {
        if (!verificarCamposFiltro())
            return null;

        FiltroBuscaInstalacao filtro = new FiltroBuscaInstalacao();
        filtro.setBairro(fldBairro.getText());
        filtro.setCidade(fldCidade.getText());
        filtro.setModalidade(cbModalidade.getSelectionModel().getSelectedItem());
        filtro.setUF(fldEstado.getText());
        filtro.setQtdEstrelas(Double.valueOf(rating.getRating()).intValue());

        if (fldValor.getText() == null || fldValor.getText().isBlank())
            filtro.setValorMaximo(null);
        else
            filtro.setValorMaximo(Double.parseDouble(fldValor.getText()));

        return filtro;
    }


    private void inserirInstalacao(Instalacao instalacao) {
        ItemReservaInstalacao content = new ItemReservaInstalacao();
        content.setNome(instalacao.getNome() + " - " + instalacao.getBairro() + ", " + instalacao.getCidade());
        content.setDescricao(instalacao.getDescricao());
        content.setValorDiaria(instalacao.getValor());
        content.adicionarModalidade(instalacao.getModalidade().getNome());
        content.setLblBtnAcao("Reservar");
        content.onClickBtnAcao((actionEvent -> abrirTelaReserva(instalacao)));


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

    /**
     * Verifica se há pelo menos um filtro
     *
     * @return
     */
    private boolean verificarCamposFiltro() {
        if (cbModalidade.getSelectionModel().getSelectedIndex() > 0)
            return true;

        if (dpPeriodo.getValue() != null)
            return true;

        if (fldEstado.getText() != null && !fldEstado.getText().isBlank())
            return true;

        if (fldCidade.getText() != null && !fldCidade.getText().isBlank())
            return true;

        if (fldBairro.getText() != null && !fldBairro.getText().isBlank())
            return true;

        if (fldValor.getText() != null && !fldValor.getText().isBlank())
            return true;

        if (rating.getRating() > 0)
            return true;

        return false;
    }

    private void abrirTelaReserva(Instalacao instalacao) {
        try {
            JFXLoaderBean<ReservarInstalacaoController> bean = FXUtils.loadWindow(ReservarInstalacaoController.class, getPrimaryStage().getWidth(), getPrimaryStage().getHeight());
            bean.getControlller().setInstalacao(instalacao);
            bean.getScene().setFill(Color.rgb(0, 0, 0, 0.5));

            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Reservar");
            stage.setScene(bean.getScene());
            stage.setX(getPrimaryStage().getX());
            stage.setY(getPrimaryStage().getY());
            stage.initOwner(getPrimaryStage());
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

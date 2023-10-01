package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.model.Avaliacao;
import com.ucs.esporteconecta.model.Instalacao;
import com.ucs.esporteconecta.model.Modalidade;
import com.ucs.esporteconecta.view.component.ItemReservaInstalacao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.math.RoundingMode.HALF_DOWN;

public class ListaInstalacoesReservarController {
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

    @FXML
    private void buscarInstalacoes() {
        // pega lista de instalações filtrada conforme parâmetros recebidos
        // para cada instalação, criar um item na lista com os dados da instalação

        Instalacao instalacao = new Instalacao();
        instalacao.setNome("Instalação");
        instalacao.setCidade("Caxias do sul");
        instalacao.setBairro("Centro");
        instalacao.setValor(100d);
        instalacao.setDescricao("Quadra fechada equipada com redes para volêi e bolas disponíveis para futsal, volêi e handbal. " +
                "Suporta 500 pessoas sentadas nas arquibancadas.");
        Modalidade modalidade = new Modalidade();
        modalidade.setNome("Futsal");
        modalidade.setUrlImagem("https://github.com/bsgabriel/EsporteConecta/blob/main/src/main/resources/com/ucs/esporteconecta/images/futsal.jpeg?raw=true");

        for (int x = 0; x < 6; x++) {
            Avaliacao avaliacao = new Avaliacao();
            avaliacao.setNota(ThreadLocalRandom.current().nextInt(0, 5));
            instalacao.getAvaliacoes().add(avaliacao);
        }

        ItemReservaInstalacao content = new ItemReservaInstalacao();
        content.setNome(instalacao.getNome() + " - " + instalacao.getBairro() + ", " + instalacao.getCidade());
        content.setDescricao(instalacao.getDescricao());
        content.setValorDiaria(instalacao.getValor());
        content.adicionarModalidade(modalidade.getNome());
        content.adicionaImagem(modalidade.getUrlImagem());

        // TODO gabriel: achar um meio de descobrir quando a barra vertial aparece. Quando ela aparecer, diminuir a largura dela

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

package com.ucs.esporteconecta.view.component;

import com.ucs.esporteconecta.view.ViewResourceHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.controlsfx.control.Rating;

import java.io.IOException;

public class ItemReservaInstalacao extends HBox {
    @FXML
    private ImageView imgViewInstalacao;

    @FXML
    private Label lblEndereco;

    @FXML
    private HBox pnlModalidades;

    @FXML
    private Label lblDescricao;

    @FXML
    private Rating rating;

    @FXML
    private Label lblNota;

    @FXML
    private Hyperlink linkAvaliacoes;

    @FXML
    private Label lblValorDiaria;

    public ItemReservaInstalacao() {
        FXMLLoader loader = new FXMLLoader(ViewResourceHelper.getComponentFxml(ItemReservaInstalacao.class));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setNome(String nome) {
        this.lblEndereco.setText(nome);
    }

    public void setDescricao(String descricao) {
        this.lblDescricao.setText(descricao);
    }

    public void setValorDiaria(Double vlr) {
        if (vlr == null)
            this.lblValorDiaria.setText("R$ 0,00 / dia");
        else
            this.lblValorDiaria.setText("R$ <vlr> / dia".replace("<vlr>", vlr.toString()));
    }

    public void adicionarModalidade(String modalidade) {
        this.pnlModalidades.getChildren().add(new Label(modalidade));
    }

    public void setAvaliacao(Double avaliacao) {
        if (avaliacao == null) {
            this.rating.setRating(0);
            this.lblNota.setText("0,0");
            return;
        }

        this.rating.setRating(avaliacao);
        this.lblNota.setText(String.format("%.1f",avaliacao).replace(".", ","));
    }


}

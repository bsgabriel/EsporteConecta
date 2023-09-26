package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.model.Instalacao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ListaInstalacoesController {

    @FXML
    private ChoiceBox selectModalidade;

    @FXML
    private DatePicker dpPeriodo;

    @FXML
    private TextField inputValor;

    @FXML
    private TextField inputEstado;

    @FXML
    private TextField inputCidade;

    @FXML
    private TextField inputBairro;

    @FXML
    private ListView listview;

    private ImageView getImagem(){
        ImageView imagemView = new ImageView();
        String caminhoImagem = "https://ceu.sme.prefeitura.sp.gov.br/wp-content/uploads/2021/11/Voleibol-640x350.jpg";
        Image imagem = new Image(caminhoImagem);
        imagemView.setImage(imagem);
        imagemView.getStyleClass().add("imagem");
        imagemView.setFitHeight(100.0);
        imagemView.setFitWidth(150.0);
        return imagemView;
    }

    private VBox getDadosInstalacao(Instalacao instalacao){
        VBox dados = new VBox();
        Label nome = new Label(instalacao.getNome().toUpperCase());
        nome.getStyleClass().add("descricao");
        Label descricao = new Label(instalacao.getDescricao());
        Label valor = new Label(Double.toString(instalacao.getValor()));
        dados.getChildren().add(nome);
        dados.getChildren().add(descricao);
        dados.getChildren().add(valor);
        dados.getStyleClass().add("teste");
        return dados;
    }

    public void onClickBtnBuscar(ActionEvent actionEvent) {
        System.out.println(this.selectModalidade.getValue());
        System.out.println(this.dpPeriodo.getValue());
        System.out.println(this.inputValor.getText());
        System.out.println(this.inputEstado.getText());
        System.out.println(this.inputCidade.getText());
        System.out.println(this.inputBairro.getText());

        // pega lista de instalações filtrada conforme parâmetros recebidos
        // para cada instalação, criar um item na lista com os dados da instalação
        HBox itemInstalacao = new HBox();
        ImageView imagemView = getImagem();
        itemInstalacao.getChildren().add(imagemView);
        Instalacao instalacao = new Instalacao();
        instalacao.setNome("Teste");
        instalacao.setDescricao("Quadra esportiva coberta para 35 pessoas");
        instalacao.setValor(520.0);
        VBox dadosInstalacao = getDadosInstalacao(instalacao);
        itemInstalacao.getChildren().add(dadosInstalacao);

        this.listview.getItems().add(itemInstalacao);

    }
}

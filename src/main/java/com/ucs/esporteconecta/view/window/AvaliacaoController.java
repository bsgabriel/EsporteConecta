package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.database.dao.AvaliacaoDAO;
import com.ucs.esporteconecta.database.dao.InstalacaoDAO;
import com.ucs.esporteconecta.database.dao.UsuarioDAO;
import com.ucs.esporteconecta.model.Avaliacao;
import com.ucs.esporteconecta.model.Esportista;
import com.ucs.esporteconecta.model.Instalacao;
import com.ucs.esporteconecta.model.Usuario;
import com.ucs.esporteconecta.util.GlobalData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.controlsfx.control.Rating;

import java.net.URL;
import java.util.ResourceBundle;

import static com.ucs.esporteconecta.util.DialogHelper.showErrorDialog;
import static com.ucs.esporteconecta.util.DialogHelper.showInformation;

public class AvaliacaoController implements Initializable {

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
    private TextArea fldComentario;
    private Instalacao instalacao;
    private InstalacaoDAO instalacaoDAO;
    private AvaliacaoDAO avaliacaoDAO;
    private UsuarioDAO usuarioDAO;

    private InstalacaoDAO getInstalacaoDAO() {
        if (instalacaoDAO == null)
            instalacaoDAO = new InstalacaoDAO();
        return instalacaoDAO;
    }

    private AvaliacaoDAO getAvaliacaoDAO() {
        if (avaliacaoDAO == null)
            avaliacaoDAO = new AvaliacaoDAO();
        return avaliacaoDAO;
    }

    private UsuarioDAO getUsuarioDAO() {
        if (usuarioDAO == null)
            usuarioDAO = new UsuarioDAO();
        return usuarioDAO;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Pegar instalação quando selecionada opção de "avaliar" na tela de lista de reservas
        int id = 2;

        this.instalacao = getInstalacaoDAO().findOne(id);
        this.lblEndereco.setText(instalacao.getNome() + " - " + instalacao.getBairro() + ", " + instalacao.getCidade());
        this.lblDescricao.setText(instalacao.getDescricao());
        this.pnlModalidades.getChildren().add(new Label(instalacao.getModalidade().getNome()));
        this.imgViewInstalacao.setImage(new Image(instalacao.getModalidade().getUrlImagem()));
    }

    @FXML
    private void avaliarInstalacao(ActionEvent event) {

        // Retirar essa linha para pegar usuário logado automaticamente
        GlobalData.setUsuarioLogado(getUsuarioDAO().buscar("csdamo", "123"));

        Usuario esportista = GlobalData.getUsuarioLogado();
        if (GlobalData.getUsuarioLogado() instanceof Esportista){
            Avaliacao avaliacao = new Avaliacao();
            avaliacao.setInstalacao(this.instalacao);
            avaliacao.setEsportista((Esportista)esportista);
            avaliacao.setNota((int)rating.getRating());
            avaliacao.setComentario(fldComentario.getText());

            if (getAvaliacaoDAO().persist(avaliacao)) {
                showInformation("Instalacao avaliada com sucesso!");
                return;
            }
        }
        showErrorDialog("Não foi possível realizar avaliação");
    }


}

package com.ucs.esporteconecta.view.component;

import com.ucs.esporteconecta.Launcher;
import com.ucs.esporteconecta.database.dao.AvaliacaoDAO;
import com.ucs.esporteconecta.database.dao.ReservaDAO;
import com.ucs.esporteconecta.listeners.OnSaveAction;
import com.ucs.esporteconecta.model.Avaliacao;
import com.ucs.esporteconecta.model.Reserva;
import com.ucs.esporteconecta.util.CustomTask;
import com.ucs.esporteconecta.util.DialogHelper;
import com.ucs.esporteconecta.view.ViewResourceHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ItemReservaEsportista extends HBox {

    @FXML
    private ImageView imgInstalacao;

    @FXML
    private Label lblDataReserva;

    @FXML
    private Label lblHoraReserva;

    @FXML
    private Label lblEndereco;

    @FXML
    private Button btnAvaliar;

    @FXML
    private Button btnCancelar;

    @FXML
    private VBox boxAvaliacao;

    @FXML
    private Rating nota;

    @FXML
    private TextArea inputComentario;

    private Reserva reserva;
    private AvaliacaoDAO avaliacaoDAO;
    private ReservaDAO reservaDAO;
    private OnSaveAction onSave;

    private AvaliacaoDAO getAvaliacaoDAO() {
        if (avaliacaoDAO == null)
            avaliacaoDAO = new AvaliacaoDAO();
        return avaliacaoDAO;
    }

    private ReservaDAO getReservaDAO() {
        if (reservaDAO == null)
            reservaDAO = new ReservaDAO();
        return reservaDAO;
    }

    public ItemReservaEsportista() {
        FXMLLoader loader = new FXMLLoader(ViewResourceHelper.getComponentFxml(ItemReservaEsportista.class));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
        if (reserva != null)
            carregarDados();
    }

    public void setOnSave(OnSaveAction onSave) {
        this.onSave = onSave;
    }

    @FXML
    private void initialize() {
        URL url = Launcher.class.getResource("images/basquete.jpg");
        if (url == null)
            System.out.println("URL nula");
        else
            imgInstalacao.setImage(new Image(url.toString()));
    }

    @FXML
    private void exibirAvaliacao() {
        carregarAvaliacoes();
        boxAvaliacao.setVisible(!boxAvaliacao.isVisible());
        boxAvaliacao.setManaged(!boxAvaliacao.isManaged());
    }

    @FXML
    private void salvarAvaliacao() {
        new CustomTask<Avaliacao>() {
            @Override
            protected Avaliacao call() throws Exception {
                Avaliacao avaliacao = getAvaliacaoDAO().buscarPorEsporstistaInstalacao(reserva.getEsportista(), reserva.getInstalacao());

                if (avaliacao == null) {
                    avaliacao = new Avaliacao();
                    avaliacao.setEsportista(reserva.getEsportista());
                    avaliacao.setInstalacao(reserva.getInstalacao());
                }

                avaliacao.setNota(Double.valueOf(nota.getRating()).intValue());
                avaliacao.setComentario(inputComentario.getText());

                if (avaliacao.getId() == null)
                    getAvaliacaoDAO().persist(avaliacao);
                else
                    getAvaliacaoDAO().update(avaliacao);

                if (onSave != null)
                    onSave.call();

                return avaliacao;
            }

            @Override
            protected void sucesso(Avaliacao avaliacao) {
                nota.setRating(avaliacao.getNota());
                inputComentario.setText(avaliacao.getComentario());
            }

            @Override
            protected void erro(Throwable throwable) {
                String msgErro = "Não foi possível salvar a avaliação referente ao esportista (%d) e instalação (%d)";
                System.err.println(String.format(msgErro, reserva.getEsportista().getId(), reserva.getInstalacao().getId()));
                throwable.printStackTrace();
            }
        }.executar(this);
    }

    @FXML
    private void cancelarReserva() {
        new CustomTask<Void>() {

            @Override
            protected Void call() throws Exception {
                getReservaDAO().removerReserva(reserva);
                return null;
            }

            @Override
            protected void sucesso(Void value) {
                DialogHelper.showInformation("Reserva cancelada com sucesso!");
                if (onSave != null)
                    onSave.call();
            }

            @Override
            protected void erro(Throwable throwable) {
                DialogHelper.showErrorDialog("Não foi possível cancelar a reserva");
                throwable.printStackTrace();
            }
        }.executar(this);
    }

    private void carregarDados() {
        lblEndereco.setText(reserva.getInstalacao().getNome() + " - " + reserva.getInstalacao().getBairro() + ", " + reserva.getInstalacao().getCidade());
        lblDataReserva.setText(reserva.getData().format(DateTimeFormatter.ofPattern("dd/MM")));
        lblHoraReserva.setText(reserva.getHorario().getInicio() + " - " + reserva.getHorario().getFim());
        carregarImagem(reserva.getInstalacao().getModalidade().getUrlImagem());
        if (LocalDate.now().isAfter(reserva.getData()) || LocalDate.now().isEqual(reserva.getData())) {
            btnCancelar.setVisible(false);
            btnCancelar.setManaged(false);
        } else {
            btnAvaliar.setVisible(false);
            btnAvaliar.setManaged(false);
        }
    }

    private void carregarAvaliacoes() {
        new CustomTask<Avaliacao>() {
            @Override
            protected Avaliacao call() throws Exception {
                return getAvaliacaoDAO().buscarPorEsporstistaInstalacao(reserva.getEsportista(), reserva.getInstalacao());
            }

            @Override
            protected void sucesso(Avaliacao avaliacao) {
                if (avaliacao == null)
                    return;

                nota.setRating(avaliacao.getNota());
                inputComentario.setText(avaliacao.getComentario());
            }

            @Override
            protected void erro(Throwable throwable) {
                String msgErro = "Não foi possível buscar a avaliação referente ao esportista (%d) e instalação (%d)";
                System.err.println(String.format(msgErro, reserva.getEsportista().getId(), reserva.getInstalacao().getId()));
                throwable.printStackTrace();
            }
        }.executar(this);
    }

    private void carregarImagem(String strURL) {
        URL url = Launcher.class.getResource(strURL);
        if (url == null)
            System.out.println("URL nula");
        else
            imgInstalacao.setImage(new Image(url.toString()));
    }

}

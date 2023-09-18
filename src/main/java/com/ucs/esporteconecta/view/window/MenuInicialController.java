package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.database.dao.UsuarioDAO;
import com.ucs.esporteconecta.model.Esportista;
import com.ucs.esporteconecta.model.Instituicao;
import com.ucs.esporteconecta.model.Usuario;
import com.ucs.esporteconecta.util.GlobalData;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Notifications;

public class MenuInicialController {

    private static final String CSS_CLASS_ALERTA = "alerta";

    @FXML
    private Label lblTitulo;

    @FXML
    private ToggleButton tglEntrar;

    @FXML
    private ToggleButton tglCadastrar;

    @FXML
    private VBox pnlCadastro;

    @FXML
    private RadioButton rdLocatario;

    @FXML
    private RadioButton rdLocador;

    @FXML
    private TextField inputRazaoSocial;

    @FXML
    private TextField inputNome;

    @FXML
    private TextField inputDocumento;

    @FXML
    private TextField inputLogin;

    @FXML
    private PasswordField inputSenha;

    private UsuarioDAO usuarioDAO;

    private UsuarioDAO getUsuarioDAO() {
        if (usuarioDAO == null)
            usuarioDAO = new UsuarioDAO();
        return usuarioDAO;
    }

    @FXML
    private void initialize() {
        tglCadastrar.selectedProperty().addListener((observableValue, oldValue, newValue) -> tglEntrar.setSelected(!newValue));
        tglEntrar.selectedProperty().addListener((observableValue, oldValue, newValue) -> tglCadastrar.setSelected(!newValue));
        rdLocador.selectedProperty().addListener((observableValue, oldValue, newValue) -> rdLocatario.setSelected(!newValue));
        rdLocatario.selectedProperty().addListener((observableValue, oldValue, newValue) -> rdLocador.setSelected(!newValue));

        pnlCadastro.visibleProperty().bind(tglCadastrar.selectedProperty());
        pnlCadastro.managedProperty().bind(tglCadastrar.selectedProperty());

        lblTitulo.textProperty().bind(Bindings.createStringBinding(() -> {
            if (tglCadastrar.isSelected())
                return "Informe seus dados";
            return "Reserve um espaço para praticar seu esporte favorito";
        }, tglCadastrar.selectedProperty()));

        inputRazaoSocial.visibleProperty().bind(rdLocador.selectedProperty());
        inputRazaoSocial.managedProperty().bind(rdLocador.selectedProperty());

        inputNome.promptTextProperty().bind(Bindings.createStringBinding(() -> {
            if (rdLocador.isSelected())
                return "Nome fantasia";
            return "Nome";
        }, rdLocador.selectedProperty()));

        inputLogin.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue)
                inputLogin.getStyleClass().remove(CSS_CLASS_ALERTA);
        });

        inputSenha.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue)
                inputSenha.getStyleClass().remove(CSS_CLASS_ALERTA);
        });
    }

    @FXML
    private void confirmar() {
        if (!validarCamposLogin())
            return;

        Usuario u = getUsuarioDAO().buscar(inputLogin.getText(), inputSenha.getText());
        if (u == null) {
            Notifications.create().position(Pos.CENTER).title("Erro").text("Usuário ou senha inválidos").showError();
            return;
        }

        GlobalData.setUsuarioLogado(u);

        notificarUsuarioLogado();
        // TODO: verificar usuário e chamr próxima tela
    }

    /**
     * Exibe um popup com informações do usuário logado. Método deverá ser removido eventualmente.
     */
    private void notificarUsuarioLogado() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usuário: ").append(GlobalData.getUsuarioLogado().getLogin()).append("\n");
        if (GlobalData.getUsuarioLogado() instanceof Instituicao instituicao) {
            sb.append("Nome fantasia: ").append(instituicao.getNomeFantasia()).append("\n");
            sb.append("Razação Social: ").append(instituicao.getRazaoSocial()).append("\n");
            sb.append("CNPJ: ").append(instituicao.getCnpj()).append("\n");
            sb.append("Instalações: ").append(instituicao.getInstalacoes().size()).append("\n");
        } else {
            Esportista esportista = (Esportista) GlobalData.getUsuarioLogado();
            sb.append("CPF: ").append(esportista.getCpf()).append("\n");
            sb.append("Reservas: ").append(esportista.getReservas().size()).append("\n");
        }

        Notifications.create().position(Pos.CENTER).title("Login efetuado com sucesso").text(sb.toString()).showInformation();
    }

    private boolean validarCamposLogin() {
        if (inputLogin.getText() == null || inputLogin.getText().isBlank()) {
            Notifications.create().position(Pos.CENTER).title("Aviso").text("Usuário não informado").showWarning();
            inputLogin.getStyleClass().add(CSS_CLASS_ALERTA);
            return false;
        }

        if (inputSenha.getText() == null || inputSenha.getText().isBlank()) {
            Notifications.create().position(Pos.CENTER).title("Aviso").text("Senha não informada").showWarning();
            inputSenha.getStyleClass().add(CSS_CLASS_ALERTA);
            return false;
        }

        return true;
    }

}

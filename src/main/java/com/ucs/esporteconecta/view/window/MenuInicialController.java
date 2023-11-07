package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.database.dao.UsuarioDAO;
import com.ucs.esporteconecta.model.Esportista;
import com.ucs.esporteconecta.model.Instituicao;
import com.ucs.esporteconecta.model.Usuario;
import com.ucs.esporteconecta.util.FXUtils;
import com.ucs.esporteconecta.util.GlobalData;
import com.ucs.esporteconecta.util.interfaces.IController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Notifications;

import java.io.IOException;

import static com.ucs.esporteconecta.util.DialogHelper.*;
import static javafx.beans.binding.Bindings.createStringBinding;

public class MenuInicialController implements IController {

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

    private Parent root;
    private UsuarioDAO usuarioDAO;

    private UsuarioDAO getUsuarioDAO() {
        if (usuarioDAO == null)
            usuarioDAO = new UsuarioDAO();
        return usuarioDAO;
    }

    @Override
    public void setRoot(Parent root) {
        this.root = root;
    }

    @FXML
    private void initialize() {
        tglCadastrar.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            tglEntrar.setSelected(!newValue);
            limparCampos();
        });

        tglEntrar.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            tglCadastrar.setSelected(!newValue);
            limparCampos();
        });

        rdLocador.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            rdLocatario.setSelected(!newValue);
            limparCampos();
        });

        rdLocatario.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            rdLocador.setSelected(!newValue);
            limparCampos();
        });

        pnlCadastro.visibleProperty().bind(tglCadastrar.selectedProperty());
        pnlCadastro.managedProperty().bind(tglCadastrar.selectedProperty());

        lblTitulo.textProperty().bind(createStringBinding(() -> {
            if (tglCadastrar.isSelected())
                return "Informe seus dados";
            return "Reserve um espaço para praticar seu esporte favorito";
        }, tglCadastrar.selectedProperty()));

        inputRazaoSocial.visibleProperty().bind(rdLocador.selectedProperty());
        inputRazaoSocial.managedProperty().bind(rdLocador.selectedProperty());

        inputNome.promptTextProperty().bind(createStringBinding(() -> {
            if (rdLocador.isSelected())
                return "Nome fantasia";
            return "Nome";
        }, rdLocador.selectedProperty()));

        inputDocumento.promptTextProperty().bind(createStringBinding(() -> {
            if (rdLocador.isSelected())
                return "CNPJ";
            return "CPF";
        }, rdLocador.selectedProperty()));

    }

    @FXML
    private void confirmar() {
        if (tglEntrar.isSelected()) {
            login();
            return;
        }

        if (rdLocador.isSelected())
            cadastrarInstituicao();
        else
            cadastrarEsportista();

    }

    private void login() {
        if (!validarCamposLogin())
            return;

        Usuario u = getUsuarioDAO().buscar(inputLogin.getText(), inputSenha.getText());
        if (u == null) {
            showErrorDialog("Usuário ou senha inválidos");
            return;
        }

        GlobalData.setUsuarioLogado(u);
//        notificarUsuarioLogado();

        try {

            Scene scene = null;

            if (GlobalData.getUsuarioLogado() instanceof Esportista esportista) {
                if (!esportista.getReservas().isEmpty())
                    scene = FXUtils.loadWindow(ListaReservasEsportistaController.class).getScene();
                else
                    scene = FXUtils.loadWindow(ListaInstalacoesReservarController.class).getScene();
            } else {
                scene = FXUtils.loadWindow(ListaInstalacoesEditarController.class).getScene();
            }

            GlobalData.getPrimaryStage().setTitle("Início");
            GlobalData.getPrimaryStage().setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cadastrarEsportista() {
        if (!validarCamposEsportista())
            return;

        Esportista e = new Esportista();
        e.setLogin(inputLogin.getText());
        e.setSenha(inputSenha.getText());
        e.setCpf(inputDocumento.getText());
        e.setNome(inputNome.getText());

        if (!getUsuarioDAO().persist(e)) {
            showErrorDialog("Não foi possível realizar cadastro");
            return;
        }

        showInformation("Usuário criado com sucesso!");
        GlobalData.setUsuarioLogado(e);
        // TODO: abrir próxima tela
    }

    private void cadastrarInstituicao() {
        if (!validarCamposInstituicao())
            return;

        Instituicao i = new Instituicao();
        i.setLogin(inputLogin.getText());
        i.setSenha(inputSenha.getText());
        i.setCnpj(inputDocumento.getText());
        i.setNomeFantasia(inputNome.getText());
        i.setRazaoSocial(inputRazaoSocial.getText());

        if (!getUsuarioDAO().persist(i)) {
            showErrorDialog("Não foi possível realizar cadastro");
            return;
        }

        showInformation("Usuário criado com sucesso!");
        GlobalData.setUsuarioLogado(i);
        // TODO: abrir próxima tela
    }

    private void limparCampos() {
        inputLogin.setText("");
        inputSenha.setText("");
        inputRazaoSocial.setText("");
        inputNome.setText("");
        inputDocumento.setText("");
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
            showWarning("Usuário não informado");
            return false;
        }

        if (inputSenha.getText() == null || inputSenha.getText().isBlank()) {
            showWarning("Senha não informada");
            return false;
        }

        return true;
    }

    private boolean validarCamposInstituicao() {
        if (inputRazaoSocial.getText() == null || inputRazaoSocial.getText().isBlank()) {
            showWarning("Razão social não informada");
            return false;
        }


        if (inputNome.getText() == null || inputNome.getText().isBlank()) {
            showWarning("Nome fantasia não informado");
            return false;
        }

        if (inputDocumento.getText() == null || inputDocumento.getText().isBlank()) {
            showWarning("CNPJ não informado");
            return false;
        }

        if (inputLogin.getText() == null || inputLogin.getText().isBlank()) {
            showWarning("Login não informado");
            return false;
        }

        if (inputSenha.getText() == null || inputSenha.getText().isBlank()) {
            showWarning("Senha não informada");
            return false;
        }

        return true;
    }

    private boolean validarCamposEsportista() {
        if (inputNome.getText() == null || inputNome.getText().isBlank()) {
            showWarning("Nome não informado");
            return false;
        }

        if (inputDocumento.getText() == null || inputDocumento.getText().isBlank()) {
            showWarning("CPF não informado");
            return false;
        }

        if (inputLogin.getText() == null || inputLogin.getText().isBlank()) {
            showWarning("Login não informado");
            return false;
        }

        if (inputSenha.getText() == null || inputSenha.getText().isBlank()) {
            showWarning("Senha não informada");
            return false;
        }

        return true;
    }

}

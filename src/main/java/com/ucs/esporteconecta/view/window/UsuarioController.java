package com.ucs.esporteconecta.view.window;

import com.ucs.esporteconecta.database.dao.UsuarioDAO;
import com.ucs.esporteconecta.model.Esportista;
import com.ucs.esporteconecta.model.Instituicao;
import com.ucs.esporteconecta.model.Usuario;
import com.ucs.esporteconecta.validator.ValidaCamposUsuarioEsportista;
import com.ucs.esporteconecta.validator.ValidaCamposUsuarioInstituicao;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;

public class UsuarioController {

    @FXML
    private RadioButton campoLocatario;

    @FXML
    private RadioButton campoLocador;

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoRazaoSocial;

    @FXML
    private TextField campoDocumento;

    @FXML
    private TextField campoLogin;

    @FXML
    private PasswordField campoSenha;

    @FXML
    void selectLocatario(ActionEvent event) {
        this.campoNome.setPromptText("Nome");
        this.campoRazaoSocial.setVisible(false);
    }

    @FXML
    void selectLocador(ActionEvent event) {
        this.campoNome.setPromptText("Nome Fantasia");
        this.campoRazaoSocial.setVisible(true);
    }

    private void limparCampos(){
        this.campoNome.clear();
        this.campoRazaoSocial.clear();
        this.campoDocumento.clear();
        this.campoLogin.clear();
        this.campoSenha.clear();
    }

    private void chamaPersistenciaDados(Usuario usuario){
        UsuarioDAO dao = new UsuarioDAO();
        try {
            dao.persist(usuario);
            Notifications.create()
                    .position(Pos.CENTER)
                    .title("Cadastro de Usuário")
                    .text("Usuário criado com sucesso")
                    .showInformation();
            limparCampos();

        } catch (Exception ex) {
            System.err.println("Erro ao persistir dados");
            ex.printStackTrace();
            Platform.exit();
            Notifications.create()
                    .position(Pos.CENTER)
                    .title("Cadastro de Usuário")
                    .text("Erro ao criar o Usuário")
                    .showError();
        }
    }

    public void criarUsuario() {

        boolean locatario = campoLocatario.isSelected(); // Esportista
        boolean locador = campoLocador.isSelected();  // Instituicao

        String nome = this.campoNome.getText();
        String razaoSocial = this.campoRazaoSocial.getText();
        String documento = this.campoDocumento.getText();
        String login = this.campoLogin.getText();
        String senha = this.campoSenha.getText();

        if (locatario){

            Esportista usuarioEsportista = new Esportista();
            usuarioEsportista.setNome(nome);
            usuarioEsportista.setCpf(documento);
            usuarioEsportista.setLogin(login);
            usuarioEsportista.setSenha(senha);

            ValidaCamposUsuarioEsportista validacaousuarioEsportista = new ValidaCamposUsuarioEsportista();
            boolean usuarioEsportistaValido = validacaousuarioEsportista.validaCampos(usuarioEsportista);

            if (usuarioEsportistaValido){
                chamaPersistenciaDados(usuarioEsportista);

            }
            else{
                Notifications.create()
                        .position(Pos.CENTER)
                        .title("Cadastro de Usuário")
                        .text("Erro ao criar o Usuário")
                        .showError();
            }

        }
        else if(locador){

            Instituicao usuarioInstituicao = new Instituicao();
            usuarioInstituicao.setNomeFantasia(nome);
            usuarioInstituicao.setRazaoSocial(razaoSocial);
            usuarioInstituicao.setCnpj(documento);
            usuarioInstituicao.setLogin(login);
            usuarioInstituicao.setSenha(senha);

            ValidaCamposUsuarioInstituicao validacaousuarioInstituicao = new ValidaCamposUsuarioInstituicao();
            boolean usuarioInstituicaoValido = validacaousuarioInstituicao.validaCampos(usuarioInstituicao);

            if (usuarioInstituicaoValido){
                chamaPersistenciaDados(usuarioInstituicao);

            }
            else{
                Notifications.create()
                        .position(Pos.CENTER)
                        .title("Cadastro de Usuário")
                        .text("Erro ao criar o Usuário")
                        .showError();
            }
        }
        else{
            Notifications.create()
                    .position(Pos.CENTER)
                    .title("Cadastro de Usuário")
                    .text("Erro ao criar o Usuário")
                    .showError();
        }
    }
}

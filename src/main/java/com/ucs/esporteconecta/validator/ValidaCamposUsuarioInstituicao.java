package com.ucs.esporteconecta.validator;

import com.ucs.esporteconecta.model.Instituicao;

public class ValidaCamposUsuarioInstituicao extends ValidaCamposUsuario{

    public boolean validaCampos(Instituicao instituicao) {
        boolean usuarioValido = true;

        if (instituicao.getNomeFantasia().isEmpty() || instituicao.getNomeFantasia().isBlank()) {
            usuarioValido = false;
        }else if (instituicao.getCnpj().isEmpty() || instituicao.getCnpj().isBlank()) {
            usuarioValido = false;
        }else if (instituicao.getRazaoSocial().isEmpty() || instituicao.getRazaoSocial().isBlank()) {
            usuarioValido = false;
        }else if (instituicao.getLogin().isEmpty() || instituicao.getLogin().isBlank()) {
            usuarioValido = false;
        }else if (instituicao.getSenha().isEmpty() || instituicao.getSenha().isBlank()) {
            usuarioValido = false;
        }
        return usuarioValido;
    }
}

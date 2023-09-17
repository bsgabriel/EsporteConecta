package com.ucs.esporteconecta.validator;

import com.ucs.esporteconecta.model.Esportista;

public class ValidaCamposUsuarioEsportista extends ValidaCamposUsuario{

    public boolean validaCampos(Esportista esportista) {
        boolean usuarioValido = true;

        if (esportista.getNome().isEmpty() || esportista.getNome().isBlank()) {
            usuarioValido = false;
        }else if (esportista.getCpf().isEmpty() || esportista.getCpf().isBlank()) {
            usuarioValido = false;
        }else if (esportista.getLogin().isEmpty() || esportista.getLogin().isBlank()) {
            usuarioValido = false;
        }else if (esportista.getSenha().isEmpty() || esportista.getSenha().isBlank()) {
            usuarioValido = false;
        }
        return usuarioValido;
    }
}

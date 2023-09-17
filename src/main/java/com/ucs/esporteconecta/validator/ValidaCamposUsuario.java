package com.ucs.esporteconecta.validator;

import com.ucs.esporteconecta.model.Usuario;

public class ValidaCamposUsuario {
    public  boolean validaCampos(Usuario usuario){
        boolean usuarioValido = true;

     if (usuario.getLogin().isEmpty() || usuario.getLogin().isBlank()) {
         System.out.println("Não tem  login de usuario");
            usuarioValido = false;
    }else if (usuario.getSenha().isEmpty() || usuario.getSenha().isBlank()) {
        usuarioValido = false;
    }
    return usuarioValido;

    }
}

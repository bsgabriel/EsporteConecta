package com.ucs.esporteconecta.testes;

import com.ucs.esporteconecta.database.dao.UsuarioDAO;
import com.ucs.esporteconecta.model.Usuario;

import java.util.List;

public class TesteBusca {
    public static void main(String[] args) {
        testeBuscaTodos();
//        testeBuscaUsuario();
    }

    private static void testeBuscaTodos() {
        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> lst = dao.buscar();

        System.out.println("Usuarios: " + lst.size());

        lst.forEach(u -> System.out.println(u.getClass()));
    }

    private static void testeBuscaUsuario() {
        UsuarioDAO dao = new UsuarioDAO();
        Usuario u = dao.buscar("ucs", "12345");

        if (u == null) {
            System.out.println("Não achou usuário");
            return;
        }

        System.out.println(u.getClass());
    }

}

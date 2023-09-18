package com.ucs.esporteconecta.util;

import com.ucs.esporteconecta.model.Usuario;

public class GlobalData {
    private static Usuario usuarioLogado;

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuarioLogado) {
        GlobalData.usuarioLogado = usuarioLogado;
    }

}

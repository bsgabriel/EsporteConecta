package com.ucs.esporteconecta.testes;

import com.ucs.esporteconecta.database.dao.ModalidadeDAO;
import com.ucs.esporteconecta.model.*;
import javafx.application.Platform;


public class TesteModalidade {

    public static void main(String[] args) {
        System.out.println("Iniciando teste...\n");

        ModalidadeDAO dao = new ModalidadeDAO();

        Modalidade modalidade = gerarModalidade("Volei");
        persistModalidade(dao, modalidade);

        Modalidade modalidade1 = gerarModalidade("Tennis");
        persistModalidade(dao, modalidade1);

        Modalidade modalidade2 = gerarModalidade("Futsal");
        persistModalidade(dao, modalidade2);

        System.out.println("Finalizou o teste\n");

    }

    private static Modalidade gerarModalidade(String nome) {
        Modalidade modalidade = new Modalidade();
        modalidade.setNome(nome);
        return modalidade;
    }

    private static void persistModalidade(ModalidadeDAO dao, Modalidade mod) {

        try {
            dao.persist(mod);
        } catch (Exception ex) {
            System.err.println("Erro ao persistir modalidade, finalizando teste");
            ex.printStackTrace();
            Platform.exit();
        }

    }


}

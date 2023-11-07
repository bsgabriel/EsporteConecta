package com.ucs.esporteconecta.testes;

import com.ucs.esporteconecta.database.dao.InstalacaoDAO;
import com.ucs.esporteconecta.database.dao.ModalidadeDAO;
import com.ucs.esporteconecta.database.dao.ReservaDAO;
import com.ucs.esporteconecta.database.dao.UsuarioDAO;
import com.ucs.esporteconecta.model.*;
import com.ucs.esporteconecta.model.enums.DiaSemana;
import javafx.application.Platform;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.math.RoundingMode.HALF_DOWN;

public class TesteCadastro {
    public static void main(String[] args) {
//        System.out.println("Iniciando teste...\n");
//        Esportista esportista = gerarEsportista();
//        Instituicao instituicao = gerarInstituicao();
//        gerarAvaliacoes(instituicao, esportista);
//
//        UsuarioDAO dao = new UsuarioDAO();
//
//        System.out.println("Persistindo esportista...\n");
//        try {
//            dao.persist(esportista);
//        } catch (Exception ex) {
//            System.err.println("Erro ao persistir esportista, finalizando teste");
//            ex.printStackTrace();
//            Platform.exit();
//        }
//
//        System.out.println("Persistindo instituição...\n");
//        try {
//            dao.persist(instituicao);
//        } catch (Exception ex) {
//            System.err.println("Erro ao persistir instituição, finalizando teste");
//            ex.printStackTrace();
//            Platform.exit();
//        }
//
//        System.out.println("Finalizou o teste\n");

        Instalacao instalacao = new InstalacaoDAO().findOne(2);
//        Esportista esportista = (Esportista) new UsuarioDAO().buscar("gabi3", "1234");
//        Reserva reserva = new Reserva();
//        reserva.setEsportista(esportista);
//        reserva.setInstalacao(instalacao);
//        reserva.setHorario(instalacao.getFuncionamentos().get(0).getHorario());
//        reserva.setData(LocalDate.now());
//        new ReservaDAO().persist(reserva);

        Reserva reserva = new ReservaDAO().buscar(instalacao, instalacao.getFuncionamentos().get(1).getHorario(), LocalDate.now());
        System.out.println(reserva);
    }

    private static DiaSemana randomDiaSemana() {
        switch (ThreadLocalRandom.current().nextInt(1, 7)) {
            case 1:
                return DiaSemana.DOMINGO;
            case 2:
                return DiaSemana.SEGUNDA_FEIRA;
            case 3:
                return DiaSemana.TERCA_FEIRA;
            case 4:
                return DiaSemana.QUARTA_FEIRA;
            case 5:
                return DiaSemana.QUINTA_FEIRA;
            case 6:
                return DiaSemana.SEXTA_FEIRA;
            case 7:
                return DiaSemana.SABADO;
            default:
                return null;
        }
    }

    private static Esportista gerarEsportista() {
        Esportista esportista = new Esportista();
        esportista.setNome("Gabriel");
        esportista.setLogin("gabi3");
        esportista.setSenha("1234");
        return esportista;
    }

    private static Instituicao gerarInstituicao() {
        Instituicao instituicao = new Instituicao();
        instituicao.setLogin("ucs3");
        instituicao.setSenha("ucs@senha");
        instituicao.setRazaoSocial("Universidade de Caxias do Sul");
        instituicao.setNomeFantasia("UCS");

        for (int i = 0; i < 10; i++) {
            instituicao.getInstalacoes().add(gerarInstalacao(i, instituicao));
        }

        return instituicao;
    }

    private static Instalacao gerarInstalacao(int idx, Instituicao instituicao) {
        Instalacao instalacao = new Instalacao();
        instalacao.setCapacidadeMaxima(10);
        instalacao.setInstituicao(instituicao);
        instalacao.setDescricao("Instalação " + (1 + idx) + " da instituição " + instituicao.getNomeFantasia());
        instalacao.setNome("Piscina olimpica");
        instalacao.setValor(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(100.0, 1001.0)).setScale(1, HALF_DOWN).doubleValue());
        instalacao.setCidade("Caxias do Sul");
        instalacao.setBairro("Exposicao");
        instalacao.setEstado("RS");

        Modalidade modalidade = new ModalidadeDAO().buscarPorId(1);
        instalacao.setModalidade(modalidade);

        DiaSemana diaSemana = randomDiaSemana();
        if (diaSemana != null) {
            Funcionamento func1 = new Funcionamento();
            func1.setDiaSemana(diaSemana);
            Horario manha = new Horario();
            manha.setInicio(LocalTime.of(8, 0));
            manha.setFim(LocalTime.of(12, 0));
            func1.setHorario(manha);
            func1.setInstalacao(instalacao);

            Funcionamento func2 = new Funcionamento();
            func2.setDiaSemana(diaSemana);
            Horario tarde = new Horario();
            tarde.setInicio(LocalTime.of(13, 30));
            tarde.setFim(LocalTime.of(18, 0));
            func2.setHorario(tarde);
            func2.setInstalacao(instalacao);

            instalacao.getFuncionamentos().add(func1);
            instalacao.getFuncionamentos().add(func2);
        }

        return instalacao;
    }

    private static void gerarAvaliacoes(final Instituicao instituicao, final Esportista esportista) {
        instituicao.getInstalacoes().forEach(instalacao -> {
            for (int i = 0; i < 10; i++) {
                int n = ThreadLocalRandom.current().nextInt(1, 6);
                Avaliacao aval = new Avaliacao();
                aval.setInstalacao(instalacao);
                aval.setEsportista(esportista);
                aval.setNota(n);
                aval.setComentario("Comentário teste " + i + 1);
                instalacao.getAvaliacoes().add(aval);
            }
        });
    }


}

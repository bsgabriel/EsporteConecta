package com.ucs.esporteconecta.database.dao;

import com.ucs.esporteconecta.database.DataBaseManager;
import com.ucs.esporteconecta.model.Funcionamento;
import com.ucs.esporteconecta.model.Instalacao;
import com.ucs.esporteconecta.model.Instituicao;
import com.ucs.esporteconecta.util.filtros.FiltroBuscaInstalacao;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

import java.util.List;

public class InstalacaoDAO {

    private DataBaseManager dataBaseManager;

    public DataBaseManager getDataBaseManager() {
        if (dataBaseManager == null) {
            dataBaseManager = new DataBaseManager();
        }
        return dataBaseManager;
    }

    public boolean persist(Object instalacao) {
        Session session = null;
        Transaction tx = null;
        try {
            session = getDataBaseManager().getFactory().openSession();
            tx = session.beginTransaction();
            session.persist(instalacao);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            System.out.println("Transação falhou : ");
            e.printStackTrace();
            return false;
        } finally {
            if (session != null)
                session.close();
        }

        return true;
    }

    public void updateFuncionamento(Session session, Funcionamento funcionamento) {

        MutationQuery qHorario = session.createMutationQuery(
                "UPDATE Horario " +
                        "SET inicio = :inicio, fim = :fim " +
                        "WHERE id = :id");

        qHorario.setParameter("inicio", funcionamento.getHorario().getInicio());
        qHorario.setParameter("fim", funcionamento.getHorario().getFim());
        qHorario.setParameter("id", funcionamento.getHorario().getId());
        qHorario.executeUpdate();

        MutationQuery q = session.createMutationQuery(
                "UPDATE Funcionamento " +
                        "SET diaSemana = :diaSemana " +
                        "WHERE id = :id");

        q.setParameter("diaSemana", funcionamento.getDiaSemana());
        q.setParameter("id", funcionamento.getId());
        q.executeUpdate();
    }

    public boolean update(Instalacao instalacao) {
        Session session = null;
        Transaction tx = null;
        try {
            session = getDataBaseManager().getFactory().openSession();
            tx = session.beginTransaction();

            for (Funcionamento func : instalacao.getFuncionamentos()) {
                updateFuncionamento(session, func);
            }

            MutationQuery q = session.createMutationQuery(
                    "UPDATE Instalacao " +
                        "SET nome = :nome, descricao = :descricao, capacidadeMaxima = :capmax, valor = :valor, bairro = :bairro, " +
                        "cidade = :cidade, estado = :estado, modalidade = :modalidade " +
                        "WHERE id = :id");

            q.setParameter("nome", instalacao.getNome());
            q.setParameter("descricao", instalacao.getDescricao());
            q.setParameter("capmax", instalacao.getCapacidadeMaxima());
            q.setParameter("valor", instalacao.getValor());
            q.setParameter("bairro", instalacao.getBairro());
            q.setParameter("cidade", instalacao.getCidade());
            q.setParameter("estado", instalacao.getEstado());
            q.setParameter("modalidade", instalacao.getModalidade());
            q.setParameter("id", instalacao.getId());
            q.executeUpdate();

            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null)
                session.close();
        }

        return true;
    }

    public List<Instalacao> findAll() {
        return getDataBaseManager().getEntityManager().createQuery("From Instalacao", Instalacao.class).getResultList();
    }

    public Instalacao findOne(int id) {
        return getDataBaseManager().getEntityManager().createQuery("From Instalacao Where id = :id", Instalacao.class)
                .setParameter("id", id).getSingleResult();
    }

    public List<Instalacao> findByInstituicao(Instituicao instituicao) {

        Query qry = getDataBaseManager().getEntityManager().createQuery("From Instalacao Where instituicao.id =: id", Instalacao.class);
        qry.setParameter("id", instituicao.getId());
        return qry.getResultList();
    }

    public List<Instalacao> find(FiltroBuscaInstalacao filtro) {
        Query qry = getDataBaseManager().getEntityManager().createQuery(createFilteredQuery(filtro), Instalacao.class);
        if (filtro.getBairro() != null && !filtro.getBairro().isBlank())
            qry.setParameter("bairro", filtro.getBairro().toLowerCase());

        if (filtro.getCidade() != null && !filtro.getCidade().isBlank())
            qry.setParameter("cidade", filtro.getCidade().toLowerCase());

        if (filtro.getUF() != null && !filtro.getUF().isBlank())
            qry.setParameter("estado", filtro.getUF().toLowerCase());

        if (filtro.getValorMaximo() != null)
            qry.setParameter("valor", filtro.getValorMaximo());

        return qry.getResultList();
    }

    private String createFilteredQuery(FiltroBuscaInstalacao filtro) {
        StringBuilder sb = new StringBuilder();
        sb.append("from Instalacao where ");
        if (filtro.getBairro() != null && !filtro.getBairro().isBlank())
            sb.append("lower(bairro) = :bairro").append(" and ");

        if (filtro.getCidade() != null && !filtro.getCidade().isBlank())
            sb.append("lower(cidade) = :cidade").append(" and ");

        if (filtro.getUF() != null && !filtro.getUF().isBlank())
            sb.append("lower(estado) = :estado").append(" and ");

        if (filtro.getValorMaximo() != null)
            sb.append("valor <= :valor").append(" and ");

        /* TODO: filtar por modalidade e estrelas.
            Para as estrelas, usar algo com AVG (ex: select AVG(nota) from avaliacoes where instalacao_id = 1;).
         */

        System.out.println("query antes: " + sb);
        if (sb.toString().endsWith(" and ")) {
            sb.delete(sb.lastIndexOf(" and"), sb.length());
        }
        System.out.println("query depois: " + sb);

        return sb.toString();
    }

}

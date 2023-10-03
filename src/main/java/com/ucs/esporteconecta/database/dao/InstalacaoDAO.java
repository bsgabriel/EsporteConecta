package com.ucs.esporteconecta.database.dao;

import com.ucs.esporteconecta.database.DataBaseManager;
import com.ucs.esporteconecta.model.Instalacao;
import com.ucs.esporteconecta.util.filtros.FiltroBuscaInstalacao;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

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

    public List<Instalacao> findAll() {
        return getDataBaseManager().getEntityManager().createQuery("From Instalacao", Instalacao.class).getResultList();
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

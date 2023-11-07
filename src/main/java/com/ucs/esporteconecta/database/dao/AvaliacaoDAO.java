package com.ucs.esporteconecta.database.dao;

import com.ucs.esporteconecta.database.DataBaseManager;
import com.ucs.esporteconecta.model.Avaliacao;
import com.ucs.esporteconecta.model.Esportista;
import com.ucs.esporteconecta.model.Instalacao;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class AvaliacaoDAO {

    private DataBaseManager dataBaseManager;

    public DataBaseManager getDataBaseManager() {
        if (dataBaseManager == null) {
            dataBaseManager = new DataBaseManager();
        }
        return dataBaseManager;
    }

    public boolean persist(Object avaliacao) {
        Session session = null;
        Transaction tx = null;

        try {
            session = getDataBaseManager().getFactory().openSession();
            tx = session.beginTransaction();
            session.persist(avaliacao);
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

    public void update(Avaliacao avaliacao) {
        String strQuery = "update Avaliacao set comentario = :comentario, nota = :nota where id = :id";
        getDataBaseManager().getEntityManager().getTransaction().begin();
        Query query = getDataBaseManager().getEntityManager().createQuery(strQuery);
        query.setParameter("comentario", avaliacao.getComentario());
        query.setParameter("nota", avaliacao.getNota());
        query.setParameter("id", avaliacao.getId());
        query.executeUpdate();
        getDataBaseManager().getEntityManager().getTransaction().commit();
    }

    public List<Avaliacao> buscarPorEsportista(Long idEsportista) {
        String strQuery = "FROM Avaliacao WHERE idEsportista = :idEsportista";
        TypedQuery<Avaliacao> qry = getDataBaseManager().getEntityManager().createQuery(strQuery, Avaliacao.class);
        qry.setParameter("idEsportista", idEsportista);
        try {
            return qry.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Avaliacao> buscarPorInstalacao(Long idInstalacao) {
        String strQuery = "FROM Avaliacao WHERE idInstalacao = :idInstalacao";
        TypedQuery<Avaliacao> qry = getDataBaseManager().getEntityManager().createQuery(strQuery, Avaliacao.class);
        qry.setParameter("idInstalacao", idInstalacao);
        try {
            return qry.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Avaliacao buscarPorEsporstistaInstalacao(Esportista esportista, Instalacao instalacao) {
        String strQuery = "FROM Avaliacao WHERE esportista = :esportista AND instalacao = :instalacao";
        TypedQuery<Avaliacao> qry = getDataBaseManager().getEntityManager().createQuery(strQuery, Avaliacao.class);
        qry.setParameter("esportista", esportista);
        qry.setParameter("instalacao", instalacao);
        try {
            return qry.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}

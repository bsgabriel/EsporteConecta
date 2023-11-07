package com.ucs.esporteconecta.database.dao;

import com.ucs.esporteconecta.database.DataBaseManager;
import com.ucs.esporteconecta.model.*;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {
    private DataBaseManager dataBaseManager;

    public DataBaseManager getDataBaseManager() {
        if (dataBaseManager == null) {
            dataBaseManager = new DataBaseManager();
        }
        return dataBaseManager;
    }

    public boolean persist(Reserva reserva) {
        Session session = null;
        Transaction tx = null;
        try {
            session = getDataBaseManager().getFactory().openSession();
            tx = session.beginTransaction();
            session.merge(reserva);
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

    public Reserva buscar(Instalacao instalacao, Horario horario, LocalDate date) {
        String hql = "SELECT r FROM Reserva r " +
                "JOIN r.instalacao i " +
                "JOIN r.horario h " +
                "WHERE i = :instalacao " +
                "AND h = :horario " +
                "AND DATE(r.data) = :data";

        Query query = getDataBaseManager().getEntityManager().createQuery(hql, Reserva.class);
        query.setParameter("instalacao", instalacao);
        query.setParameter("horario", horario);
        query.setParameter("data", date);

        Reserva reserva = null;
        try {
            reserva = (Reserva) query.getSingleResult();
        } catch (NoResultException ex) {
            System.err.println("nenhum resultado encontrado para a query");
        }

        return reserva;
    }

    public List<Reserva> buscar(Esportista esportista) {
        String hql = "SELECT r FROM Reserva r " + "JOIN r.esportista e " + "WHERE e = :esportista";

        Query query = getDataBaseManager().getEntityManager().createQuery(hql, Reserva.class);
        query.setParameter("esportista", esportista);

        List<Reserva> reservas = new ArrayList<>();
        try {
            reservas = query.getResultList();
        } catch (NoResultException ex) {
            System.err.println("nenhum resultado encontrado para a query");
        }

        return reservas;
    }

    public void removerReserva(Reserva reserva) {
        Session session = null;
        Transaction tx = null;
        try {
            session = getDataBaseManager().getFactory().openSession();
            tx = session.beginTransaction();
            session.remove(reserva);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            System.out.println("Transação falhou : ");
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }

    public List<Reserva> buscarPorInstituicao(Instituicao instituicao) {
        String hql = "SELECT r FROM Reserva r " +
                "JOIN r.instalacao i " +
                "WHERE i.instituicao.id = :instituicao AND r.data >= current timestamp " +
                "ORDER BY r.data DESC " +
                "LIMIT 5";

        Query query = getDataBaseManager().getEntityManager().createQuery(hql, Reserva.class);
        query.setParameter("instituicao", instituicao.getId());

        List<Reserva> reservas = null;
        try {
            reservas = (List<Reserva>) query.getResultList();
        } catch (NoResultException ex) {
            System.err.println("nenhum resultado encontrado para a query");
        }

        return reservas;
    }

}

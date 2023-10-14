package com.ucs.esporteconecta.database.dao;

import com.ucs.esporteconecta.database.DataBaseManager;
import com.ucs.esporteconecta.model.Horario;
import com.ucs.esporteconecta.model.Instalacao;
import com.ucs.esporteconecta.model.Reserva;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;

public class ReservaDAO {
    private DataBaseManager dataBaseManager;

    public DataBaseManager getDataBaseManager() {
        if (dataBaseManager == null) {
            dataBaseManager = new DataBaseManager();
        }
        return dataBaseManager;
    }

    public boolean persist(Reserva reserva) {
        // TODO: verificar se reserva possui instalação e esportista
        Session session = null;
        Transaction tx = null;
        try {
            session = getDataBaseManager().getFactory().openSession();
            tx = session.beginTransaction();
            session.persist(reserva);
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
}

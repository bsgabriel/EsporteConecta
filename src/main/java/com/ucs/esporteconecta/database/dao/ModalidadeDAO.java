package com.ucs.esporteconecta.database.dao;

import com.ucs.esporteconecta.database.DataBaseManager;
import com.ucs.esporteconecta.model.Modalidade;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ModalidadeDAO {

    private DataBaseManager dataBaseManager;

    public DataBaseManager getDataBaseManager() {
        if (dataBaseManager == null) {
            dataBaseManager = new DataBaseManager();
        }
        return dataBaseManager;
    }

    public boolean persist(Object modalidade) {
        Session session = null;
        Transaction tx = null;
        try {
            session =  getDataBaseManager().getFactory().openSession();
            tx = session.beginTransaction();
            session.persist(modalidade);
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

    public List<Modalidade> findAll() {
        List<Modalidade> lst = new ArrayList<>();
        Session session = null;
        try {
            session = getDataBaseManager().getFactory().openSession();
            List<Modalidade> result = session.createQuery("FROM Modalidade", Modalidade.class).getResultList();

            if (result != null) {
                lst.addAll(result);
            }
        } catch (Exception e) {
            System.out.println("Transação falhou : ");
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
        return lst;
    }

    public Modalidade buscarPorId(int id) {
        Session session = null;
        Modalidade modalidade = null;
        try {
            session =  getDataBaseManager().getFactory().openSession();
            modalidade = session.get(Modalidade.class, id);
        } catch (Exception e) {
            System.out.println("Ocoreu um erro ao obter a modalidade: ");
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }

        return modalidade;
    }

}

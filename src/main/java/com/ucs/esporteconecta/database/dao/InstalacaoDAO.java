package com.ucs.esporteconecta.database.dao;

import com.ucs.esporteconecta.database.DataBaseManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
            session =  getDataBaseManager().getFactory().openSession();
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

}

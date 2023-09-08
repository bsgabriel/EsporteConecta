package com.ucs.esporteconecta.database.dao;

import com.ucs.esporteconecta.database.DataBaseManager;
import com.ucs.esporteconecta.model.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsuarioDAO {
    private DataBaseManager dataBaseManager;

    public DataBaseManager getDataBaseManager() {
        if (dataBaseManager == null) {
            dataBaseManager = new DataBaseManager();
        }
        return dataBaseManager;
    }

    public List<Usuario> findAll() {
        List<Usuario> lst = new ArrayList<>();
        Session session = null;
        Transaction tx = null;
        try {
            session = getDataBaseManager().getFactory().openSession();
            List<Usuario> result = session.createQuery("FROM Usuario", Usuario.class).getResultList();

            if (result != null) {
                lst.addAll(result);
            }
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            System.out.println("Transação falhou : ");
            e.printStackTrace();
        } finally {
            session.close();
        }
        return lst;
    }

    public void persist(Object usuario) {
        Session session = null;
        Transaction tx = null;
        try {
            session = getDataBaseManager().getFactory().openSession();
            tx = session.beginTransaction();
            session.persist(usuario);
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
}

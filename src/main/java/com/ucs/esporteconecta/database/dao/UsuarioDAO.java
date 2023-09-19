package com.ucs.esporteconecta.database.dao;

import com.ucs.esporteconecta.database.DataBaseManager;
import com.ucs.esporteconecta.model.Usuario;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

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
        try {
            session = getDataBaseManager().getFactory().openSession();
            List<Usuario> result = session.createQuery("FROM Usuario", Usuario.class).getResultList();

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

    public boolean persist(Object usuario) {
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
            return false;
        } finally {
            if (session != null)
                session.close();
        }

        return true;
    }

    public Usuario buscar(String login, String senha) {
        TypedQuery<Usuario> qry = getDataBaseManager().getEntityManager().createQuery(" FROM Usuario u WHERE u.login = :login and u.senha = :senha", Usuario.class);
        qry.setParameter("login", login);
        qry.setParameter("senha", senha);
        try {
            return qry.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Usuario> buscar() {
        return getDataBaseManager().getEntityManager().createQuery(" from Usuario", Usuario.class).getResultList();
    }

}

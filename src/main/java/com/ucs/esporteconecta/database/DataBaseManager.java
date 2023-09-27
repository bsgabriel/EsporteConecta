package com.ucs.esporteconecta.database;

import com.ucs.esporteconecta.model.*;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DataBaseManager {
    private SessionFactory factory;
    private Configuration config;

    private EntityManager entityManager;

    public DataBaseManager() {

    }

    public EntityManager getEntityManager() {
        if (entityManager == null)
            entityManager = getFactory().createEntityManager();
        return entityManager;
    }

    private Configuration getConfig() {
        if (config == null)
            config = createConfiguration();
        return config;
    }

    private Configuration createConfiguration() {
        Configuration c = new Configuration();
        configProperties(c);
        configClasses(c);
        return c;
    }

    /**
     * Seta as propriedades do Hibernate
     *
     * @param config
     */
    private void configProperties(Configuration config) {
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        config.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        config.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/EsporteConecta");
        config.setProperty("hibernate.connection.username", "postgres");
        config.setProperty("hibernate.connection.password", "1234");
        config.setProperty("hibernate.show_sql", "true");
        config.setProperty("hibernate.hbm2ddl.auto", "update");
    }

    /**
     * Carrega as classes que representam entidades do banco
     *
     * @param config
     */
    private void configClasses(Configuration config) {
        config.addAnnotatedClass(Avaliacao.class);
        config.addAnnotatedClass(Esportista.class);
        config.addAnnotatedClass(Funcionamento.class);
        config.addAnnotatedClass(Horario.class);
        config.addAnnotatedClass(Instalacao.class);
        config.addAnnotatedClass(Instituicao.class);
        config.addAnnotatedClass(Reserva.class);
        config.addAnnotatedClass(Modalidade.class);
    }


    public SessionFactory getFactory() {
        if (factory == null)
            factory = getConfig().buildSessionFactory();
        return factory;
    }
}
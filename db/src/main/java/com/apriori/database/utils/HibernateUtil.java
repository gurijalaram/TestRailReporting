package com.apriori.database.utils;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.properties.PropertiesContext;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static Transaction transaction = null;

    public static Transaction getActiveGlobalTransaction() {
        if (transaction == null) {
            transaction = beginTransaction();
        }

        return transaction;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }

    private static Transaction beginTransaction() {
        return sessionFactory.getCurrentSession().beginTransaction();
    }

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the ServiceRegistry from hibernate.cfg.xml
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure(
                    Thread.currentThread().getContextClassLoader().getResource(String.format("%s/hibernate.cfg.xml", PropertiesContext.get("global.db_connection")))).build();

            // Create a metadata sources using the specified service registry.
            Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();

            return metadata.getSessionFactoryBuilder().build();
        } catch (Throwable ex) {

            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }


    public static void finalizeSession() {
        getSessionFactory().close();
    }
}

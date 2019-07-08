package utils;

import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
public class SessionFactoryClass<T> {

    private Class<T> entity;

    public SessionFactoryClass(Class<T> entity) {
        this.entity = entity;
    }

    public Session getSession() {
        try {
           return new Configuration()
                    .setProperties(new PropertiesHendler().getDBProperties())
                    .addAnnotatedClass(entity)
                    .buildSessionFactory().openSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

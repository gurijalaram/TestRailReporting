package utils;

import entity.User;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SessionFactoryClass<T> {

    private String dbType;
    private Class<T> entity;

    public SessionFactoryClass(String dbType, Class<T> entity) {
        this.dbType = dbType;
        this.entity = entity;
    }

    public String getDbType() {
        return dbType;
    }

    public Class<T> getEntity() {
        return entity;
    }

    public Session getSession() {
        Properties properties = new Properties();
        InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(getDbType().concat(".hibernate.properties"));
        try {
            properties.load(resourceStream);
            try {
                return new Configuration()
                        .setProperties(properties)
                        .addAnnotatedClass(entity)
                        .buildSessionFactory().openSession();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

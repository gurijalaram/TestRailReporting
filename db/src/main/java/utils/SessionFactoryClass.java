package utils;

import org.hibernate.cfg.Configuration;
import org.hibernate.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SessionFactoryClass {

    private String dbType;

    public String getDbType() {
        return dbType;
    }

    public SessionFactoryClass(String dbType) {
        this.dbType = dbType;
    }

    public Session getSession() {
        Properties properties = new Properties();
        InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(getDbType().concat(".hibernate.properties"));

        try {
            properties.load(resourceStream);
            try {
                return new Configuration()
                        .setProperties(properties)
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

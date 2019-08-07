package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesHandler {

    private static Properties DBproperties;

    public String setDBProperties(String dbType) {
        InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(dbType.concat(".hibernate.properties"));
        Properties properties = new Properties();
        try {
            properties.load(resourceStream);
            DBproperties = properties;
            return dbType;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Properties getDBProperties() {
        return DBproperties;
    }

    public PropertiesHandler() {
    }
}

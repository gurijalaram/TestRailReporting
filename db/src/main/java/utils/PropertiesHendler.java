package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesHendler {

    private static Properties DBproperties;

    public void setDBProperties(String dbType) {
        InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(dbType.concat(".hibernate.properties"));
        Properties properties = new Properties();
        try {
            properties.load(resourceStream);
            DBproperties = properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getDBProperties() {
        return DBproperties;
    }

    public PropertiesHendler() {
    }

}

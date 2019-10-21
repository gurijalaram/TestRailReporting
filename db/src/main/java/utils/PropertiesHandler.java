package utils;

import java.io.IOException;
import java.util.Properties;

import org.hibernate.cfg.Configuration;


public class PropertiesHandler {

    private static Properties DBproperties;
    
    public void setDBProperties(String dbType) {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getSystemResourceAsStream("aServerProps.properties"));
            properties.putAll(new Configuration().addResource("hibernate.cfg.xml").configure().getProperties());
            System.getProperties().setProperty("com.apriori.home.dir",properties.getProperty("com.apriori.home.dir"));
            System.getProperties().setProperty("apriori.test.properties.path",properties.getProperty("apriori.test.properties.path"));
            System.getProperties().setProperty("fbc.hibernate.dir",properties.getProperty("fbc.hibernate.dir"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DBproperties = properties;  
    }

    public Properties getDBProperties() {
        return DBproperties;
    }

    public PropertiesHandler() {}
}

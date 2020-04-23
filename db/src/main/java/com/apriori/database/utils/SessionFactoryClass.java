package com.apriori.database.utils;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.net.URL;

public class SessionFactoryClass {

    public SessionFactoryClass() {
    }

    public Session getSession() {
        try {
            Configuration config = new Configuration()
                    .setProperties(new PropertiesHandler().getDBProperties());
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL entityesUrl = loader.getResource("com/apriori/database/entity");
            File folder = new File(entityesUrl.getPath());
            File[] classes = folder.listFiles();
            for (File file : classes) {
                int index = file.getName().indexOf(".");
                String className = file.getName().substring(0, index);
                String classNamePath = "com/apriori/database/entity" + "." + className;
                Class<?> repoClass = Class.forName(classNamePath);
                config.addAnnotatedClass(repoClass);
            }

            return config.buildSessionFactory().openSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

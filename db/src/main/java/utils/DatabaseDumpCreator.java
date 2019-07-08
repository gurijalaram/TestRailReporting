package utils;

import java.io.IOException;
import java.util.Properties;

public class DatabaseDumpCreator {

    private String dbType;

    public String getDbType() {
        return dbType;
    }

    public DatabaseDumpCreator(String dbType) {
        this.dbType = dbType;
    }

    public void createMySqlDBdump() {

        Properties DBprop = new PropertiesHendler().getDBProperties();
        String executeCmd = "mysqldump -u"
                + DBprop.getProperty("hibernate.connection.username")
                + " -p" + DBprop.getProperty("hibernate.connection.password")
                + " " + DBprop.getProperty("hibernate.DBname")
                + " --ignore-table="+DBprop.getProperty("hibernate.DBname")+".fbc_licensemodule"
                + " --ignore-table="+DBprop.getProperty("hibernate.DBname")+".fbc_siteinfo"
                + " --ignore-table="+DBprop.getProperty("hibernate.DBname")+".fbc_user"
                + " --ignore-table="+DBprop.getProperty("hibernate.DBname")+".fbc_usergroupassoc"
                + " --ignore-table="+DBprop.getProperty("hibernate.DBname")+".fbc_userlicense"
                + " -r mySQL"+ DBprop.getProperty("hibernate.DBname") +"_dump.sql";

        try {
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                System.out.println("DB backup taken successfully");
            } else {
                System.out.println("Could not take DB backup");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public DatabaseDumpCreator() {
    }
}

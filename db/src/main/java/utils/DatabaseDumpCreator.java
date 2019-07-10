package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class DatabaseDumpCreator {
   private Properties DBprop = new PropertiesHendler().getDBProperties();

    public void createTestMySqlDBdump() {
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
        createDump(executeCmd);
    }

    public void createMySqlDBdump(ArrayList <String> excludedTablesList){
        StringBuffer executeCmd = initDBRequest();
        for (int i = 0; i < excludedTablesList.size(); i++) {
            executeCmd.append(" --ignore-table=" + DBprop.getProperty("hibernate.DBname") + "." + excludedTablesList.get(i));
        }
        executeCmd.append(" -r mySQL" + DBprop.getProperty("hibernate.DBname") + "_dump.sql");
        createDump(executeCmd.toString());
    }

    private StringBuffer initDBRequest() {
        return new StringBuffer().append( "mysqldump -u"
                + DBprop.getProperty("hibernate.connection.username")
                + " -p" + DBprop.getProperty("hibernate.connection.password")
                + " " + DBprop.getProperty("hibernate.DBname"));
    }

    private void createDump(String executeCmd){
        try {
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                System.out.println("DB dump successfully created");
            } else {
                System.out.println("Could not create DB dump");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public DatabaseDumpCreator() {}
}

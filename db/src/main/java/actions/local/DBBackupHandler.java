package actions.local;

import utils.PropertiesHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DBBackupHandler {

    private PropertiesHandler dataBasePropHandler = new PropertiesHandler();
    private String dbType;
    private ArrayList<String> excludedTables;
    private String userProjectHomePath;

    public DBBackupHandler(ArrayList<String> excludedTables) {
        this.dbType = initDB();
        this.excludedTables = excludedTables;
    }

    private String initDB() {
        return dataBasePropHandler.getDBProperties().getProperty("hibernate.connection.url").split(":")[1];
    }

    private StringBuffer connectionStringMaker() {
        StringBuffer connectionString = null;
        if (dbType.equals("mysql")) {
            System.out.println("mysql connection string");
            connectionString = new StringBuffer().append("mysqldump -u"
                    + dataBasePropHandler.getDBProperties().getProperty("hibernate.connection.username")
                    + " -p" + dataBasePropHandler.getDBProperties().getProperty("hibernate.connection.password")
                    + " " + dataBasePropHandler.getDBProperties().getProperty("hibernate.connection.dbname"));
            for (int i = 0; i < excludedTables.size(); i++) {
                connectionString.append(" --ignore-table=" + dataBasePropHandler.getDBProperties().getProperty("hibernate.connection.dbname") + "." + excludedTables.get(i).concat(" -r "));
            }
            connectionString.append(backupPathHandlere());
            System.out.println(connectionString);
        } else if (dbType.equals("sqlserver")) {
            System.out.println("sqlserver connection string");
            connectionString = new StringBuffer().append("sqlcmd -q ").append("\"").append("BACKUP DATABASE apriori TO DISK = ").append(backupPathHandlere()).append("\"");
            System.out.println(connectionString);
        } else if (dbType.equals("oracle")) {
            System.out.println("oracle connection string");
        }
        return connectionString;
    }

    private String backupPathHandlere() {
        if (dbType.equals("sqlserver")) {
            userProjectHomePath = "'".concat(System.getProperty("user.dir").concat("\\apriori.bak")).concat("'");
        } else if (dbType.equals("mysql")) {
            userProjectHomePath = dataBasePropHandler.getDBProperties().getProperty("hibernate.connection.dbname") + "_dump.sql";
        } else if (dbType.equals("oracle")) {
            System.out.println("oracle");
        } else {
            System.out.println("Couldn't resolve DB type");
        }
        return userProjectHomePath;
    }

    private void databaseBackupStringExecutor(StringBuffer connectionString) {
        try {
            File dbBackup = new File(userProjectHomePath.replace("'", ""));
            if (dbBackup.exists()) {
                dbBackup.delete();
                System.out.println("Old backup file was deleted");
            }
            Runtime.getRuntime().exec(connectionString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createDataBaseBackup() {
        StringBuffer connectionString = connectionStringMaker();
        databaseBackupStringExecutor(connectionString);
    }
}

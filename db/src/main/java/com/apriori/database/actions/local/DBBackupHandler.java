package com.apriori.database.actions.local;

import com.apriori.database.utils.PropertiesHandler;

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
        switch (dbType) {
            case "mysql":
                System.out.println("mysql connection string");
                connectionString = new StringBuffer().append("mysqldump -u").append(dataBasePropHandler.getDBProperties().getProperty("hibernate.connection.username")).append(" -p").append(dataBasePropHandler.getDBProperties().getProperty("hibernate.connection.password")).append(" ").append(dataBasePropHandler.getDBProperties().getProperty("hibernate.connection.dbname"));
                for (String excludedTable : excludedTables) {
                    connectionString.append(" --ignore-table=").append(dataBasePropHandler.getDBProperties().getProperty("hibernate.connection.dbname")).append(".").append(excludedTable.concat(" -r "));
                }
                connectionString.append(backupPathHandlere());
                System.out.println(connectionString);
                break;
            case "sqlserver":
                System.out.println("sqlserver connection string");
                connectionString = new StringBuffer().append("sqlcmd -q ").append("\"").append("BACKUP DATABASE apriori TO DISK = ").append(backupPathHandlere()).append("\"");
                System.out.println(connectionString);
                break;
            case "oracle":
                System.out.println("oracle connection string");
                break;
        }
        return connectionString;
    }

    private String backupPathHandlere() {
        switch (dbType) {
            case "sqlserver":
                userProjectHomePath = "'".concat(System.getProperty("user.dir").concat("\\apriori.bak")).concat("'");
                break;
            case "mysql":
                userProjectHomePath = dataBasePropHandler.getDBProperties().getProperty("hibernate.connection.dbname") + "_dump.sql";
                break;
            case "oracle":
                System.out.println("oracle");
                break;
            default:
                System.out.println("Couldn't resolve DB type");
                break;
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

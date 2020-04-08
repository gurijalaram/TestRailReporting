import actions.local.DBBackupHandler;
import actions.local.DBDataImporter;

import org.junit.Test;

import utils.PropertiesHandler;

import java.util.ArrayList;

public class DBworkTests {
    static {
        /* set the type of db, for all tests: mysql/mssql/oracle */
        new PropertiesHandler().setDBProperties("mysql");
    }

    @Test
    public void createDBdump() {
        ArrayList<String> excludedTables = new ArrayList<>();
        excludedTables.add("fbc_persistentdocument");
        DBBackupHandler dbBackupMaker = new DBBackupHandler(excludedTables);
        dbBackupMaker.createDataBaseBackup();
    }

    @Test
    public void importIntoDB() {
        DBDataImporter dbDataImporter = new DBDataImporter();
        /* Path to folder with test materials */
        String[] partPath = new String[]{"C:\\Users\\ssakho.FBC\\Desktop\\test\\ap"};
        dbDataImporter.importFilesIntoDB(partPath);
        System.out.println("Data was successfully imported");
    }
}

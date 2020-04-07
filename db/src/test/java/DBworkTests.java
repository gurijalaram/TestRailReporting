import org.junit.Test;
import utils.DBBackupHandler;
import utils.DBDataImporter;
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
        DBBackupHandler db_beckup_maker = new DBBackupHandler(excludedTables);
        db_beckup_maker.createDataBaseBackup();
    }

    @Test
    public void importIntoDB() {
        DBDataImporter dbDataImporter = new DBDataImporter();
        /* Path to folder with test materials */
        String [] partPath = new String [] {"C:\\Users\\ssakho.FBC\\Desktop\\test\\ap"};
        dbDataImporter.imporFilesIntoDB(partPath);
        System.out.println("Data was successfully imported");
    }
}

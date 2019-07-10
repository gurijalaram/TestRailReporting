import org.junit.Test;
import utils.DatabaseDumpCreator;
import utils.PropertiesHendler;


import java.util.ArrayList;

public class DBworkTests {

    static {
        /*
            set the type of db, for all tests:
             - mysql
             - mssql
             - oracle
        */
        new PropertiesHendler().setDBProperties("mysql");
    }

    @Test
    public void createStandardDBdump() {
        DatabaseDumpCreator databaseDumpCreator = new DatabaseDumpCreator();
        /* Create standard dump without tables: fbc_licensemodule, fbc_siteinfo, fbc_user, fbc_usergroupassoc, fbc_userlicense */
        databaseDumpCreator.createTestMySqlDBdump();
    }

    @Test
    public void createDBdump() {
        DatabaseDumpCreator databaseDumpCreator = new DatabaseDumpCreator();
        ArrayList<String> excludedTablesList = new ArrayList<String>();
        excludedTablesList.add("fbc_user");
        excludedTablesList.add("fbc_persistentdocument");
        /* createMySqlDBdump - create dump without tables, names of which were added into "excludedTablesList" */
        databaseDumpCreator.createMySqlDBdump(excludedTablesList);
    }



}

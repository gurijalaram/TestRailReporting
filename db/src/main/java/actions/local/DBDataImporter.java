package actions.local;

import com.apriori.testutil.testdata.DataLoader;

import java.io.IOException;

public class DBDataImporter {

    public void importFilesIntoDB(String[] directories) {
        try {
            DataLoader.loadFiles(directories);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

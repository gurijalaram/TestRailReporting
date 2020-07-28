package evaluate;

import com.apriori.apibase.services.PropertyStore;
import com.apriori.apibase.services.cid.objects.request.NewPartRequest;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.FileUploadResources;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FileUploadApiTest extends TestUtil {

    private static PropertyStore propertyStore;

    private final HashMap<String, String> token = new APIAuthentication().initAuthorizationHeaderNoContent("cfrith@apriori.com");

    @BeforeClass
    public static void testSetup() {
        propertyStore = (PropertyStore) JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile("property-store.json").getPath(), PropertyStore.class);
    }

    @Test
    @Description("Upload, cost and publish a part using CID API")
    public void createFileUpload() {
        Object fileObject = JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("CreatePartData.json").getPath(), NewPartRequest.class);

        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FileResourceUtil.getResourceAsFile("test-parts2.csv")))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=([^\\\"]|\\\"[^\\\"]*\\\")*$)");
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        for (List<String> record : records) {
            new FileUploadResources().uploadCostPublishApi(token, fileObject, record.get(0), record.get(1), record.get(2), record.get(3).replace("\"", ""));
        }
    }
}

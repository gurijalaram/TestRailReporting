package evaluate;

import com.apriori.apibase.services.PropertyStore;
import com.apriori.apibase.services.cis.objects.requests.NewPartRequest;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.FileUploadResources;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;


public class FileUploadApiTest extends TestUtil {

    private static PropertyStore propertyStore;

    private final HashMap<String, String> token = new APIAuthentication().initAuthorizationHeaderNoContent("cfrith@apriori.com");

    @BeforeClass
    public static void testSetup() {
        propertyStore = (PropertyStore) JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile("property-store.json").getPath(), PropertyStore.class);
    }

    @Test
    @Description("Initialize file upload using CID API")
    public void createFileUpload() {
        Object fileObject = JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("CreatePartData.json").getPath(), NewPartRequest.class);

        new FileUploadResources().createFileUpload(token, fileObject);
    }
}

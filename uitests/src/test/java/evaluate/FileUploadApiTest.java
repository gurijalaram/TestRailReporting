package evaluate;

import com.apriori.apibase.services.PropertyStore;
import com.apriori.apibase.services.cid.objects.request.NewPartRequest;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.FileUploadResources;
import com.apriori.utils.json.utils.JsonManager;
import com.apriori.utils.users.UserUtil;

import io.qameta.allure.Description;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import junitparams.mappers.IdentityMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(JUnitParamsRunner.class)
public class FileUploadApiTest extends TestUtil {

    private static PropertyStore propertyStore;

    private final HashMap<String, String> token = new APIAuthentication().initAuthorizationHeaderNoContent(UserUtil.getUser().getUsername());

    @BeforeClass
    public static void testSetup() {
        propertyStore = (PropertyStore) JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile("property-store.json").getPath(), PropertyStore.class);
    }

    @Test
    @FileParameters(value = "classpath:test-parts2.csv", mapper = CustomMapper.class, encoding = "ISO-8859-1")
    @Description("Upload, cost and publish a part using CID API")
    public void createFileUpload(String fileName, String scenarioName, String processGroup, String materialName) {
        Object fileObject = JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("CreatePartData.json").getPath(), NewPartRequest.class);

        new FileUploadResources().uploadCostPublishApi(token, fileObject, fileName, scenarioName, processGroup, materialName);
    }

    public static class CustomMapper extends IdentityMapper {
        @Override
        public Object[] map(Reader reader) {
            Object[] map = super.map(reader);
            List<Object> result = new ArrayList<>();
            for (Object lineObj : map) {
                String line = lineObj.toString();
                String[] index = line.split(",(?=([^\\\"]|\\\"[^\\\"]*\\\")*$)");
                String fileName = index[0].replace("\"", "");
                String scenarioName = index[1].replace("\"", "");
                String processGroup = index[2].replace("\"", "");
                String materialName = index[3].replace("\"", "");
                result.add(new Object[] {fileName, scenarioName, processGroup, materialName});
            }
            return result.toArray();
        }
    }
}

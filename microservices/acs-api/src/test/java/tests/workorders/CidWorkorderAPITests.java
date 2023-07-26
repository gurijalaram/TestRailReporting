package tests.workorders;

import com.apriori.FileResourceUtil;
import com.apriori.TestUtil;
import com.apriori.acs.entity.request.workorders.NewPartRequest;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.json.JsonManager;

import io.qameta.allure.Description;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import junitparams.mappers.IdentityMapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnitParamsRunner.class)
public class CidWorkorderAPITests extends TestUtil {

    @Test
    @FileParameters(value = "classpath:auto_api_upload.csv", mapper = CustomMapper.class, encoding = "ISO-8859-1")
    @Description("Upload, cost and publish a part using CID API")
    public void createDataUploadApi(String fileName, String scenarioName, String processGroup) {
        NewPartRequest productionInfoInputs = JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                        "CreatePartData.json"
                ).getPath(), NewPartRequest.class
        );

        FileUploadResources fileUploadResources = new FileUploadResources();
        FileResponse fileResponse = fileUploadResources.initializePartUpload(
                fileName,
                processGroup
        );
        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(fileResponse, scenarioName);

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        fileUploadResources.publishPart(costOutputs);
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
                result.add(new Object[] {fileName, scenarioName, processGroup});
            }
            return result.toArray();
        }
    }
}

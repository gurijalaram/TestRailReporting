package com.apriori;

import com.apriori.apibase.services.PropertyStore;
import com.apriori.apibase.services.cid.objects.request.NewPartRequest;
import com.apriori.apibase.services.fms.objects.FileResponse;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.entity.response.upload.FileUploadOutputs;
import com.apriori.utils.Constants;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.FileUploadResources;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import junitparams.mappers.IdentityMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnitParamsRunner.class)
public class CidWorkorderAPITests extends TestUtil {

    private static PropertyStore propertyStore;

    @BeforeClass
    public static void testSetup() {
        Constants.getDefaultUrl();
        propertyStore = (PropertyStore) JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile("property-store.json").getPath(), PropertyStore.class);
    }

    @Test
    @FileParameters(value = "classpath:auto_api_upload.csv", mapper = CustomMapper.class, encoding = "ISO-8859-1")
    @Description("Upload, cost and publish a part using CID API")
    public void createDataUploadApi(String fileName, String scenarioName, String processGroup) {
        Object productionInfoInputs = JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                        "CreatePartData.json"
                ).getPath(), NewPartRequest.class
        );

        FileUploadResources fileUploadResources = new FileUploadResources();
        FileResponse fileResponse = fileUploadResources.initialisePartUpload(
                fileName,
                processGroup
        );
        FileUploadOutputs fileUploadOutputs = fileUploadResources.uploadPart(fileResponse, scenarioName);

        CostOrderStatusOutputs costOutputs = fileUploadResources.costPart(productionInfoInputs, fileUploadOutputs, processGroup);

        fileUploadResources.publishPart(costOutputs);

        //new FileUploadResources().uploadCostPublishApi(productionInfoInputs, fileName, scenarioName, processGroup);
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

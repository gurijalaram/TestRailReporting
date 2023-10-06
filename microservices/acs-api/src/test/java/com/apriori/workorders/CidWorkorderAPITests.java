package com.apriori.workorders;

import com.apriori.acs.models.request.workorders.NewPartRequest;
import com.apriori.acs.models.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.models.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.fms.models.response.FileResponse;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.json.JsonManager;
import com.apriori.rules.TestRulesApi;

import io.qameta.allure.Description;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@ExtendWith(TestRulesApi.class)
public class CidWorkorderAPITests extends TestUtil {

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/auto_api_upload.csv")
    @Description("Upload, cost and publish a part using CID API")
    public void createDataUploadApi(String fileName, String scenarioName, String processGroup) {
        NewPartRequest productionInfoInputs = JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile("CreatePartData.json").getPath(), NewPartRequest.class);

        FileUploadResources fileUploadResources = new FileUploadResources();
        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            fileName,
            processGroup);
        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(fileResponse, scenarioName);

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false);

        fileUploadResources.publishPart(costOutputs);
    }
}

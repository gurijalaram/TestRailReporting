package tests.acs;

import com.apriori.acs.entity.request.workorders.NewPartRequest;
import com.apriori.acs.entity.response.acs.GcdProperties.PropertiesToReset;
import com.apriori.acs.entity.response.acs.GcdProperties.PropertiesToSet;
import com.apriori.acs.entity.response.acs.GcdTypes.GcdTypesResponse;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import tests.workorders.WorkorderAPITests;
import testsuites.categories.AcsTest;

import java.util.Collections;

public class GcdPropertiesTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "17203")
    @Description("Get save GCD Properties for Sheet Metal")
    public void testSaveGcdPropertiesSheetMetal() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();
        WorkorderAPITests workorderAPITests = new WorkorderAPITests();
        NewPartRequest productionInfoInputs = workorderAPITests.setupProductionInfoInputs();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.SHEET_METAL.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "bracket_basic.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        CostOrderStatusOutputs costOutputs = fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );

        PropertiesToSet propertiesToSet = PropertiesToSet.builder()
            .tolerance("tolerance")
            .build();

        PropertiesToReset roughnessRz = PropertiesToReset.builder()
            .roughnessRz("roughnessRz")
            .build();

        GcdTypesResponse response = acsResources.saveGcdProperties(
            costOutputs.getScenarioIterationKey(),
            "SimpleHole:2",
            propertiesToSet,
            //or use Arrays.asList with more than one
            Collections.singletonList(roughnessRz)
        );
    }
}

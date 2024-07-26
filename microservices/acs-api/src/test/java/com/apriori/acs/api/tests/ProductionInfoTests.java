package com.apriori.acs.api.tests;

import com.apriori.acs.api.models.response.acs.genericclasses.GenericResourceCreatedIdResponse;
import com.apriori.acs.api.models.response.acs.productioninfo.ProductionInfoResponse;
import com.apriori.acs.api.models.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.acs.api.utils.workorders.FileUploadResources;
import com.apriori.fms.api.models.response.FileResponse;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ProductionInfoTests extends TestUtil {
    private FileUploadResources fileUploadResources;
    private SoftAssertions softAssertions;
    private AcsResources acsResources;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        acsResources = new AcsResources(requestEntityUtil);
        fileUploadResources = new FileUploadResources(requestEntityUtil);
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = 15430)
    @Description("Get Set Production Info Test")
    public void testGetSetProductionInfo() {
        String testScenarioName = new GenerateStringUtil().generateStringForAutomation("Scenario");

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

        ProductionInfoResponse productionInfoPreChanges = acsResources.getProductionInfo(
            fileUploadOutputs.getScenarioIterationKey()
        );

        Integer newAnnualVolume = productionInfoPreChanges.getAnnualVolume() - 10;
        productionInfoPreChanges.setAnnualVolume(newAnnualVolume);

        String newMachiningMode = "NOT_MACHINED";
        productionInfoPreChanges.setMachiningMode(newMachiningMode);

        GenericResourceCreatedIdResponse genericResourceCreatedIdResponse = acsResources.setProductionInfo(
            productionInfoPreChanges,
            fileUploadOutputs.getScenarioIterationKey()
        );

        softAssertions.assertThat(genericResourceCreatedIdResponse.getId()).isNotNull();
        softAssertions.assertThat(genericResourceCreatedIdResponse.getResourceCreated()).isEqualTo("true");

        ProductionInfoResponse productionInfoPostChanges = acsResources.getProductionInfo(
            fileUploadOutputs.getScenarioIterationKey()
        );

        softAssertions.assertThat(productionInfoPostChanges.getAnnualVolume()).isEqualTo(newAnnualVolume);
        softAssertions.assertThat(productionInfoPostChanges.getMachiningMode()).isEqualTo(newMachiningMode);
        softAssertions.assertAll();
    }
}

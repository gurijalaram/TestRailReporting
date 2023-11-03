package com.apriori.acs.api.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.api.models.response.acs.genericclasses.GenericResourceCreatedIdResponse;
import com.apriori.acs.api.models.response.acs.productioninfo.ProductionInfoResponse;
import com.apriori.acs.api.models.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.acs.api.utils.workorders.FileUploadResources;
import com.apriori.fms.api.models.response.FileResponse;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ProductionInfoTests extends TestUtil {

    @Test
    @TestRail(id = 15430)
    @Description("Get Set Production Info Test")
    public void testGetSetProductionInfo() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();

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
        assertThat(genericResourceCreatedIdResponse.getId(), is(notNullValue()));
        assertThat(genericResourceCreatedIdResponse.getResourceCreated(), is(equalTo("true")));

        ProductionInfoResponse productionInfoPostChanges = acsResources.getProductionInfo(
            fileUploadOutputs.getScenarioIterationKey()
        );

        assertThat(productionInfoPostChanges.getAnnualVolume(), is(equalTo(newAnnualVolume)));
        assertThat(productionInfoPostChanges.getMachiningMode(), is(equalTo(newMachiningMode)));
    }
}

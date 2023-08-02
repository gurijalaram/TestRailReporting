package com.apriori;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.models.response.acs.genericclasses.GenericResourceCreatedIdResponse;
import com.apriori.acs.models.response.acs.productioninfo.ProductionInfoResponse;
import com.apriori.acs.models.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.fms.models.response.FileResponse;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

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

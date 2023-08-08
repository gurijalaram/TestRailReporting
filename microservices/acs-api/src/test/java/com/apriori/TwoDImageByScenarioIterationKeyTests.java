package com.apriori;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.models.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.fms.models.response.FileResponse;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;

public class TwoDImageByScenarioIterationKeyTests extends TestUtil {

    @Test
    @TestRail(id = 10902)
    @Description("Validate Get 2D Image by Scenario Iteration Key Endpoint")
    public void testGet2DImageByScenarioIterationKey() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.CASTING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "Casting.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(fileResponse, testScenarioName);

        String base64Image = acsResources.get2DImageByScenarioIterationKey(fileUploadOutputs.getScenarioIterationKey());

        assertThat(Base64.isBase64(base64Image), is(equalTo(true)));
    }
}

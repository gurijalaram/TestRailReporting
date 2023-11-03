package com.apriori.acs.api.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
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

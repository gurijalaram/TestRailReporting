package tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.apriori.acs.utils.AcsResources;
import com.apriori.entity.response.upload.FileResponse;
import com.apriori.entity.response.upload.FileUploadOutputs;
import com.apriori.utils.FileUploadResources;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import io.qameta.allure.Description;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class Get2DImageByScenarioIterationKeyTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10902")
    @Description("Validate Get 2D Image by Scenario Iteration Key Endpoint")
    public void testGet2DImageByScenarioIterationKey() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.CASTING.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initialisePartUpload(
            "Casting.prt",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(fileResponse, testScenarioName);

        List<String> infoToGetImage = new ArrayList<>();
        infoToGetImage.add(fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getWorkspaceId().toString());
        infoToGetImage.add(fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getTypeName());
        infoToGetImage.add(fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getMasterName());
        infoToGetImage.add(fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getStateName());
        infoToGetImage.add(fileUploadOutputs.getScenarioIterationKey().getIteration().toString());

        String base64Image = acsResources.get2DImageByScenarioIterationKey(infoToGetImage);

        assertThat(Base64.isBase64(base64Image), is(equalTo(true)));
    }
}

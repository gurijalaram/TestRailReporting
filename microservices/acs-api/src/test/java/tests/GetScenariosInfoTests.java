package tests;

import com.apriori.acs.entity.response.getscenariosinfo.GetScenariosInfoItem;
import com.apriori.acs.entity.response.getscenariosinfo.GetScenariosInfoResponse;
import com.apriori.acs.utils.AcsResources;
import com.apriori.entity.response.upload.FileResponse;
import com.apriori.entity.response.upload.FileUploadOutputs;
import com.apriori.entity.response.upload.ScenarioIterationKey;
import com.apriori.entity.response.upload.ScenarioKey;
import com.apriori.utils.FileUploadResources;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

public class GetScenariosInfoTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "9597")
    @Description("Validate Get Scenarios Info")
    public void testGetScenariosInfo() {
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String castingPartFileName = "Casting.prt";
        String tabFormsPartFileName = "tab_forms.prt";

        FileUploadResources fileUploadResources = new FileUploadResources();
        FileResponse fileResponseOne = fileUploadResources.initialisePartUpload(
                castingPartFileName,
                ProcessGroupEnum.CASTING.getProcessGroup()
        );
        FileUploadOutputs fileUploadOutputsOne = fileUploadResources.createFileUploadWorkorderSuppressError(fileResponseOne, testScenarioName);

        FileResponse fileResponseTwo = fileUploadResources.initialisePartUpload(
                tabFormsPartFileName,
                ProcessGroupEnum.SHEET_METAL.getProcessGroup()
        );
        FileUploadOutputs fileUploadOutputsTwo = fileUploadResources.createFileUploadWorkorderSuppressError(fileResponseTwo, testScenarioName);

        ScenarioIterationKey keyOne = fileUploadOutputsOne.getScenarioIterationKey();
        ScenarioIterationKey keyTwo = fileUploadOutputsTwo.getScenarioIterationKey();

        AcsResources acsResources = new AcsResources();
        ResponseWrapper<GetScenariosInfoResponse> response = acsResources.getScenariosInformation(
                keyOne,
                keyTwo
        );

        GetScenariosInfoItem response1 = response.getResponseEntity().get(0);
        ScenarioKey responseOneScenarioKey = response1.getScenarioIterationKey().getScenarioKey();

        GetScenariosInfoItem response2 = response.getResponseEntity().get(1);
        ScenarioKey responseTwoScenarioKey = response2.getScenarioIterationKey().getScenarioKey();

        String userToExpect = "qa-automation-01";
        String componentTypeToExpect = "PART";
        String typeNameToExpect = "partState";

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(!response1.getInitialized());
        softAssertions.assertThat(!response1.getMissing());
        softAssertions.assertThat(!response1.getVirtual());
        softAssertions.assertThat(!response1.getLocked());

        softAssertions.assertThat(response1.getCreatedBy().equals(userToExpect));
        softAssertions.assertThat(response1.getUpdatedBy().equals(userToExpect));

        softAssertions.assertThat(response1.getComponentName().equals(castingPartFileName.substring(0, 7)));
        softAssertions.assertThat(response1.getComponentType().equals(componentTypeToExpect));
        softAssertions.assertThat(response1.getFileName().equals(castingPartFileName.toLowerCase()));

        softAssertions.assertThat(responseOneScenarioKey.getMasterName().equals(castingPartFileName.substring(0, 7).toUpperCase()));
        softAssertions.assertThat(responseOneScenarioKey.getTypeName().equals(typeNameToExpect));

        softAssertions.assertThat(!response2.getInitialized());
        softAssertions.assertThat(!response2.getMissing());
        softAssertions.assertThat(!response2.getVirtual());
        softAssertions.assertThat(!response2.getLocked());

        softAssertions.assertThat(response2.getCreatedBy().equals(userToExpect));
        softAssertions.assertThat(response2.getUpdatedBy().equals(userToExpect));

        softAssertions.assertThat(response2.getComponentName().equals(tabFormsPartFileName.substring(0, 9)));
        softAssertions.assertThat(response2.getComponentType().equals(componentTypeToExpect));
        softAssertions.assertThat(response2.getFileName().equals(tabFormsPartFileName));

        softAssertions.assertThat(responseTwoScenarioKey.getMasterName().equals(tabFormsPartFileName.substring(0, 9).toUpperCase()));
        softAssertions.assertThat(responseTwoScenarioKey.getTypeName().equals(typeNameToExpect));

        softAssertions.assertAll();
    }
}

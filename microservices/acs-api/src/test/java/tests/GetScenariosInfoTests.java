package tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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

        assertThat(response1.getInitialized(), is(equalTo(false)));
        assertThat(response1.getMissing(), is(equalTo(false)));
        assertThat(response1.getVirtual(), is(equalTo(false)));
        assertThat(response1.getLocked(), is(equalTo(false)));

        assertThat(response1.getCreatedBy(), is(equalTo(userToExpect)));
        assertThat(response1.getUpdatedBy(), is(equalTo(userToExpect)));

        assertThat(response1.getComponentName(), is(equalTo(castingPartFileName.substring(0, 7))));
        assertThat(response1.getComponentType(), is(equalTo(componentTypeToExpect)));
        assertThat(response1.getFileName(), is(equalTo(castingPartFileName.toLowerCase())));

        assertThat(responseOneScenarioKey.getMasterName(),
                is(equalTo(castingPartFileName.substring(0, 7).toUpperCase())));
        assertThat(responseOneScenarioKey.getTypeName(), is(equalTo(typeNameToExpect)));

        assertThat(response2.getInitialized(), is(equalTo(false)));
        assertThat(response2.getMissing(), is(equalTo(false)));
        assertThat(response2.getVirtual(), is(equalTo(false)));
        assertThat(response2.getLocked(), is(equalTo(false)));

        assertThat(response2.getCreatedBy(), is(equalTo(userToExpect)));
        assertThat(response2.getUpdatedBy(), is(equalTo(userToExpect)));

        assertThat(response2.getComponentName(), is(equalTo(tabFormsPartFileName.substring(0, 9))));
        assertThat(response2.getComponentType(), is(equalTo(componentTypeToExpect)));
        assertThat(response2.getFileName(), is(equalTo(tabFormsPartFileName)));

        assertThat(responseTwoScenarioKey.getMasterName(),
                is(equalTo(tabFormsPartFileName.substring(0, 9).toUpperCase())));
        assertThat(responseTwoScenarioKey.getTypeName(), is(equalTo(typeNameToExpect)));
    }
}

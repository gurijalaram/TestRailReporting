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

        FileUploadResources fileUploadResources = new FileUploadResources();
        FileResponse fileResponseOne = fileUploadResources.initialisePartUpload(
                "Casting.prt",
                ProcessGroupEnum.CASTING.getProcessGroup()
        );
        FileUploadOutputs fileUploadOutputsOne = fileUploadResources.createFileUploadWorkorderSuppressError(fileResponseOne, testScenarioName);

        FileResponse fileResponseTwo = fileUploadResources.initialisePartUpload(
                "tab_forms.prt",
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
        GetScenariosInfoItem response2 = response.getResponseEntity().get(1);

        assertThat(response1.getInitialized(), is(equalTo(false)));
        assertThat(response1.getMissing(), is(equalTo(false)));
        assertThat(response1.getVirtual(), is(equalTo(false)));
        assertThat(response1.getLocked(), is(equalTo(false)));
        assertThat(response1.getCreatedBy(), is(equalTo("qa-automation-01")));
        assertThat(response1.getUpdatedBy(), is(equalTo("qa-automation-01")));
        assertThat(response1.getComponentName(), is(equalTo("Casting")));
        assertThat(response1.getComponentType(), is(equalTo("PART")));
        assertThat(response1.getFileName(), is(equalTo("casting.prt")));
        assertThat(response1.getScenarioIterationKey().getScenarioKey().getMasterName(), is(equalTo("CASTING")));
        assertThat(response1.getScenarioIterationKey().getScenarioKey().getTypeName(), is(equalTo("partState")));

        assertThat(response2.getInitialized(), is(equalTo(false)));
        assertThat(response2.getMissing(), is(equalTo(false)));
        assertThat(response2.getVirtual(), is(equalTo(false)));
        assertThat(response2.getLocked(), is(equalTo(false)));
        assertThat(response2.getCreatedBy(), is(equalTo("qa-automation-01")));
        assertThat(response2.getUpdatedBy(), is(equalTo("qa-automation-01")));
        assertThat(response2.getComponentName(), is(equalTo("tab_forms")));
        assertThat(response2.getComponentType(), is(equalTo("PART")));
        assertThat(response2.getFileName(), is(equalTo("tab_forms.prt")));
        assertThat(response2.getScenarioIterationKey().getScenarioKey().getMasterName(), is(equalTo("TAB_FORMS")));
        assertThat(response2.getScenarioIterationKey().getScenarioKey().getTypeName(), is(equalTo("partState")));
    }
}

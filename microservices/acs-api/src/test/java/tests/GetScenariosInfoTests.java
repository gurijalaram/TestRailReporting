package tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

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
        /*
         * 1 - do file upload twice and save scenario iteration key each time
         * 2 - call get scenarios info endpoint
         * 3 - assert on get scenarios info response
         */
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
        AcsResources acsResources = new AcsResources();
        ResponseWrapper<GetScenariosInfoResponse> response = acsResources.getScenariosInformation(
                fileUploadOutputsOne.getScenarioIterationKey(),
                fileUploadOutputsTwo.getScenarioIterationKey()
        );

        assertThat(response, is(notNullValue()));
    }
}

package tests.acs;

import com.apriori.acs.entity.response.acs.designGuidance.DesignGuidanceResponse;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.junit.Test;

public class DesignGuidanceTests {

    @Test
    @TestRail(testCaseId = "")
    @Description("Test Get Design Guidance for Casting - Die")
    public void testGetDesignGuidanceCastingDie() {
        FileUploadResources fileUploadResources = new FileUploadResources();
        AcsResources acsResources = new AcsResources();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        String processGroup = ProcessGroupEnum.CASTING_DIE.getProcessGroup();
        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            "DTCCastingIssues.catpart",
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        DesignGuidanceResponse designGuidanceResponse = acsResources.getDesignGuidance(
            fileUploadOutputs.getScenarioIterationKey(),
            "DTC_MESSAGES"
        );
    }
}

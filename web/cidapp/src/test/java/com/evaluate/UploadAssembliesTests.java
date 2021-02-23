package com.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class UploadAssembliesTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private File resourceFile;
    private UserCredentials currentUser;

    public UploadAssembliesTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = "5616")
    @Description("Upload Assembly file with no missing sub-components")
    public void uploadAssembliesComponentsTests() {
        File bigRingComp = FileResourceUtil.getResourceAsFile("big ring.SLDPRT");
        File pinComp = FileResourceUtil.getResourceAsFile("pin.SLDPRT");
        File smallRingComp = FileResourceUtil.getResourceAsFile("small ring.SLDPRT");
        File hingeAsm = FileResourceUtil.getResourceAsFile("Hinge assembly.SLDASM");

        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), bigRingComp, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();
        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));

        evaluatePage.uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), smallRingComp, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();
        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));

        evaluatePage.uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), pinComp, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();
        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));

        evaluatePage.uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), hingeAsm, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();
        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));
    }
}

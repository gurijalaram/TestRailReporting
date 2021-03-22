package com.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class CostScenarioTests extends TestBase {

    private File resourceFile;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;

    @Test
    @Category(SmokeTests.class)
    @Description("Cost Scenario")
    public void testCostScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum,"Casting.prt");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class);

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UNCOSTED_SCENARIO.getCostingText()), is(true));

        evaluatePage.inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));
    }
}

package com.evaluate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class UploadAssembliesTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ComponentsListPage componentsListPage;

    public UploadAssembliesTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = "5612")
    @Description("Upload Assembly file with no missing sub-components")
    public void uploadAssembliesComponentsTests() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        File bigRingComp = FileResourceUtil.getResourceAsFile("big ring.SLDPRT");
        File pinComp = FileResourceUtil.getResourceAsFile("pin.SLDPRT");
        File smallRingComp = FileResourceUtil.getResourceAsFile("small ring.SLDPRT");
        File hingeAsm = FileResourceUtil.getResourceAsFile("Hinge assembly.SLDASM");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(scenarioName, bigRingComp, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialSelectorTable()
            .search("Aluminum, Cast")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit()
            .costScenario();
        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));

        evaluatePage.uploadComponentAndSubmit(scenarioName, smallRingComp, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialSelectorTable()
            .search("Aluminum, Cast")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit()
            .costScenario();
        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));

        evaluatePage.uploadComponentAndSubmit(scenarioName, pinComp, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .openMaterialSelectorTable()
            .search("Aluminum, Cast")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit()
            .costScenario();
        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));

        evaluatePage.uploadComponentAndSubmit(scenarioName, hingeAsm, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.ASSEMBLY.getProcessGroup())
            .costScenario();
        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.UP_TO_DATE.getCostingText()), is(true));

        assertThat(evaluatePage.getComponentResults("Total"), is(equalTo("3")));
        assertThat(evaluatePage.getComponentResults("Unique"), is(equalTo("3")));
        assertThat(evaluatePage.getComponentResults("Uncosted Unique"), is(equalTo("0")));

        componentsListPage = evaluatePage.openComponents();
        assertThat(componentsListPage.getRowText("Small Ring", "Initial"), hasItems("$1.92", "Casting - Die"));
        assertThat(componentsListPage.getRowIcon("Small Ring", "Initial"), hasItems("cube", "check"));
        assertThat(componentsListPage.getRowText("Big Ring", "Initial"), hasItems("$2.19", "Casting - Die"));
        assertThat(componentsListPage.getRowIcon("Big Ring", "Initial"), hasItems("cube", "check"));
        assertThat(componentsListPage.getRowText("Pin", "Initial"), hasItems("$1.97", "Casting - Die"));
        assertThat(componentsListPage.getRowIcon("Pin", "Initial"), hasItems("cube", "check"));
    }
}

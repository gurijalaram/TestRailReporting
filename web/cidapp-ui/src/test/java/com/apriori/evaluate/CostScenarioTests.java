package com.apriori.evaluate;

import static com.apriori.testconfig.TestSuiteType.TestSuite.CUSTOMER;
import static com.apriori.testconfig.TestSuiteType.TestSuite.SANITY;

import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.NewCostingLabelEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.utils.StatusIconEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.io.File;

public class CostScenarioTests extends TestBaseUI {

    private File resourceFile;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private UserCredentials currentUser;

    public CostScenarioTests() {
        super();
    }

    @Test
    @Tags({
        @Tag(SANITY)
    })
    @TestRail(id = {8891})
    @Description("Cost Scenario")
    public void testCostScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.NOT_COSTED)).isEqualTo(true);

        evaluatePage.selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.VERIFIED)).isEqualTo((true));

        softAssertions.assertAll();
    }

    @Test
    @Tag(CUSTOMER)
    @TestRail(id = {})
    @Description("Validate component fields and cost scenario")
    public void testCostScenarioWithFieldsValidation() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser);

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.NOT_COSTED)).isEqualTo(true);

        evaluatePage.selectProcessGroup(processGroupEnum)
                .openMaterialSelectorTable()
                .search("ANSI AL380")
                .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
                .submit(EvaluatePage.class)
                .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);
        softAssertions.assertThat(evaluatePage.isIconDisplayed(StatusIconEnum.VERIFIED)).isEqualTo((true));

        softAssertions.assertAll();
    }
}

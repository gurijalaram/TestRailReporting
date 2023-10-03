package com.apriori.evaluate;

import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.pageobjects.evaluate.ChangeSummaryPage;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.evaluate.inputs.AdvancedPage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.pageobjects.evaluate.CostHistoryPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.io.File;

public class CostHistoryTests extends TestBaseUI {
    private File resourceFile;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private CostHistoryPage costHistoryPage;

    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private SoftAssertions softAssertions = new SoftAssertions();
    private UserCredentials currentUser;

    public CostHistoryTests() { super(); }

    @Test
    @TestRail(id = {})
    @Description("Verify Cost History available")
    public void testCostScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

//        ComponentInfoBuilder castingPart = componentsUtil.postComponentQueryCID(
//            ComponentInfoBuilder.builder()
//                .resourceFile(resourceFile)
//                .scenarioName(scenarioName)
//                .processGroup(processGroupEnum)
//                .build());
//
//        scenariosUtil.postCostScenario(castingPart);
//        castingPart.setCostingTemplate(
//            CostingTemplate.builder()
//                .processGroupName()
//                .build());

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            //.openScenario(castingPart.getComponentName(), castingPart.getScenarioName());
                .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser);

        //ToDo:- Change this to false when they bring that bit in
        softAssertions.assertThat(evaluatePage.isProgressButtonEnabled()).as("Verify Progress button disabled before initial cost").isTrue();

        evaluatePage.selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .costScenario()
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.COPPER_UNS_C11000.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .enterAnnualVolume("4096")
            .enterAnnualYears("8")
            .costScenario()
            .goToAdvancedTab()
            .enterBatchSize("777")
            .goToBasicTab()
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToHeatTreatmentTab()
            .selectSecondaryProcess("Certification")
            .submit(AdvancedPage.class)
            .goToBasicTab()
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToSurfaceTreatmentTab()
            .highlightSecondaryProcess("Degrease")
        ;

        costHistoryPage = evaluatePage.clickProgress();
        ChangeSummaryPage change = costHistoryPage.openChangeSummary(2);

        softAssertions.assertThat(change.leftColHeader()).as("Verify Left Column is Iteration 1").isEqualTo("Iteration 1");
        softAssertions.assertThat(change.rightColHeader()).as("Verify Right Column is Iteration 1").isEqualTo("Iteration 2");

        softAssertions.assertThat(costHistoryPage.iterationCount()).as("count").isEqualTo(6);

        softAssertions.assertAll();

    }
}

package com.apriori.evaluate;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.response.CostingTemplate;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.models.response.SecondaryDigitalFactories;
import com.apriori.models.response.SecondaryProcesses;
import com.apriori.pageobjects.evaluate.ChangeSummaryPage;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.pageobjects.evaluate.CostHistoryPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

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

        ComponentInfoBuilder castingPart = componentsUtil.postComponentQueryCID(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .resourceFile(resourceFile)
                .scenarioName(scenarioName)
                .processGroup(processGroupEnum)
                .user(currentUser)
                .build());

        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
                .build());
        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .processGroupName(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
                .annualVolume(4096)
                .build());
        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .materialName(MaterialNameEnum.COPPER_UNS_C11000.getMaterialName())
                .build());
        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .secondaryProcesses(SecondaryProcesses.builder()
                    .heatTreatment(Arrays.asList("Certification"))
                    .build())
                .build());
        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .vpeName("aPriori Ireland")
                .build());
        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .vpeName("aPriori Ireland")
                .build());
        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .annualVolume(289)
                .build());
        scenariosUtil.postCostScenario(castingPart);
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .batchSize(275)
                .build());
        scenariosUtil.postCostScenario(castingPart);
        SecondaryDigitalFactories secondaryDF = new SecondaryDigitalFactories();
        secondaryDF.setHeatTreatment("aPriori Finland");
        secondaryDF.setMachining("aPriori Germany");
        secondaryDF.setSurfaceTreatment("aPriori United Kingdom");
        secondaryDF.setOtherSecondaryProcesses("aPriori Canada");
        castingPart.setCostingTemplate(
            CostingTemplate.builder()
                .usePrimaryDigitalFactoryAsDefaultForSecondaryDigitalFactories(Boolean.FALSE)
                .secondaryDigitalFactories(secondaryDF)
                .build());
        scenariosUtil.postCostScenario(castingPart);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openScenario(castingPart.getComponentName(), castingPart.getScenarioName());
            //.uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser);

        softAssertions.assertThat(evaluatePage.isProgressButtonEnabled()).as("Verify Progress button disabled before initial cost").isTrue();

//        evaluatePage.selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
//            .costScenario()
//            .openMaterialSelectorTable()
//            .selectMaterial(MaterialNameEnum.COPPER_UNS_C11000.getMaterialName())
//            .submit(EvaluatePage.class)
//            .costScenario()
//            .enterAnnualVolume("4096")
//            .enterAnnualYears("8")
//            .costScenario()
//            .goToAdvancedTab()
//            .enterBatchSize("777")
//            .goToBasicTab()
//            .costScenario()
//            .goToAdvancedTab()
//            .openSecondaryProcesses()
//            .goToHeatTreatmentTab()
//            .selectSecondaryProcess("Certification")
//            .submit(AdvancedPage.class)
//            .goToBasicTab()
//            .costScenario()
//            .goToAdvancedTab()
//            .openSecondaryProcesses()
//            .goToSurfaceTreatmentTab()
//            .highlightSecondaryProcess("Degrease")
//            .submit(AdvancedPage.class)
//            .goToBasicTab()
//            .costScenario();

        costHistoryPage = evaluatePage.clickProgress();
        ChangeSummaryPage changeSummary = costHistoryPage.openChangeSummary(2);

        softAssertions.assertThat(changeSummary.changedFromHeader()).as("Verify Left Column is Iteration 1").isEqualTo("Iteration 1");
        softAssertions.assertThat(changeSummary.changedToHeader()).as("Verify Right Column is Iteration 1").isEqualTo("Iteration 2");

        softAssertions.assertThat(changeSummary.getChangedFrom("Batch Size")).as("Verify changed from in Batch Size").isEqualTo("-");
        softAssertions.assertThat(changeSummary.getChangedTo("Batch Size")).as("Verify changed to in Batch Size").isEqualTo("458");

        softAssertions.assertThat(costHistoryPage.iterationCount()).as("count").isEqualTo(11);

        changeSummary = changeSummary.close()
            .openChangeSummary(7);

        softAssertions.assertThat(changeSummary.getChangedFrom("Digital Factory")).as("Verify changed from in Batch Size").isEqualTo("aPriori USA");
        softAssertions.assertThat(changeSummary.getChangedTo("Digital Factory")).as("Verify changed to in Batch Size").isEqualTo("aPriori Ireland");

        softAssertions.assertAll();

    }
}

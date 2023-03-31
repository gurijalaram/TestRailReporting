package com.cic.tests;

import com.apriori.pagedata.CostingServiceSettingsData;
import com.apriori.pages.home.CIConnectHome;
import com.apriori.pages.home.settings.CostingServiceSettings;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CostingPreferencesTests extends TestBase {

    private UserCredentials currentUser = UserUtil.getUser();
    private SoftAssertions softAssertions;
    private CostingServiceSettingsData costingServiceSettingsData;
    private CIConnectHome ciConnectHome;


    public CostingPreferencesTests() {
        super();
    }

    @Before
    public void setup() {
        softAssertions = new SoftAssertions();
        ciConnectHome = new CicLoginPage(driver).login(currentUser);

    }

    @Test
    @TestRail(testCaseId = {"4006", "4426"})
    @Description("Check the Costing Preferences Model Dialog and Save settings")
    public void testCostingSettingsPreferences() {
        costingServiceSettingsData = CostingServiceSettingsData.builder()
            .scenarioName("SN" + new GenerateStringUtil().getRandomNumbersSpecLength(5))
            .processGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .digitalFactory(DigitalFactoryEnum.APRIORI_CHINA.getDigitalFactory())
            .batchSize(Integer.parseInt(new GenerateStringUtil().getRandomNumbersSpecLength(2)))
            .annualVolume(Integer.parseInt(new GenerateStringUtil().getRandomNumbersSpecLength(4)))
            .productionVolume(Integer.parseInt(new GenerateStringUtil().getRandomNumbersSpecLength(2)))
            .build();

        ciConnectHome.clickCostingServiceSettings()
            .enterScenarioName(costingServiceSettingsData.getScenarioName())
            .selectProcessGroup(costingServiceSettingsData.getProcessGroup())
            .selectDigitalFactory(costingServiceSettingsData.getDigitalFactory())
            .enterBatchSize(costingServiceSettingsData.getBatchSize().toString())
            .enterAnnualVolume(costingServiceSettingsData.getAnnualVolume().toString())
            .enterProductionLife(costingServiceSettingsData.getProductionVolume().toString())
            .clickSaveButton();

        softAssertions.assertThat(ciConnectHome.getStatusMessage()).contains("Costing Settings updated");

        CostingServiceSettings costingServiceSettings = ciConnectHome.clickCostingServiceSettings();

        softAssertions.assertThat(costingServiceSettings.getScenarioName()).isEqualTo(costingServiceSettingsData.getScenarioName());
        softAssertions.assertThat(costingServiceSettings.getProcessGroup()).isEqualTo(costingServiceSettingsData.getProcessGroup());
        softAssertions.assertThat(costingServiceSettings.getDigitalFactory()).isEqualTo(costingServiceSettingsData.getDigitalFactory());
        softAssertions.assertThat(Integer.parseInt(costingServiceSettings.getAnnualVolume())).isEqualTo(costingServiceSettingsData.getAnnualVolume());
        softAssertions.assertThat(Integer.parseInt(costingServiceSettings.getBatchSize())).isEqualTo(costingServiceSettingsData.getBatchSize());
    }

    @Test
    @TestRail(testCaseId = {"4019"})
    @Description("If Cancel is clicked then any changes made in that Modal Session are not persisted")
    public void testCancelCostingSettingsPreferences() {
        CostingServiceSettings costingServiceSettings = ciConnectHome.clickCostingServiceSettings();
        CostingServiceSettingsData costingSettingsExpectedData = CostingServiceSettingsData.builder()
            .scenarioName(costingServiceSettings.getScenarioName())
            .processGroup(costingServiceSettings.getProcessGroup())
            .digitalFactory(costingServiceSettings.getDigitalFactory())
            .batchSize(Integer.parseInt(costingServiceSettings.getBatchSize()))
            .annualVolume(Integer.parseInt(costingServiceSettings.getAnnualVolume()))
            .productionVolume(Integer.parseInt(costingServiceSettings.getProductionLife()))
            .build();

        costingServiceSettings.enterScenarioName("SN" + new GenerateStringUtil().getRandomNumbersSpecLength(5))
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .enterBatchSize(new GenerateStringUtil().getRandomNumbersSpecLength(2))
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_MEXICO.getDigitalFactory())
            .enterAnnualVolume(new GenerateStringUtil().getRandomNumbersSpecLength(4))
            .enterProductionLife(new GenerateStringUtil().getRandomNumbersSpecLength(2))
            .clickCancelButton();

        costingServiceSettings = ciConnectHome.clickCostingServiceSettings();

        softAssertions.assertThat(costingServiceSettings.getScenarioName()).isEqualTo(costingSettingsExpectedData.getScenarioName());
        softAssertions.assertThat(costingServiceSettings.getProcessGroup()).isEqualTo(costingSettingsExpectedData.getProcessGroup());
        softAssertions.assertThat(costingServiceSettings.getDigitalFactory()).isEqualTo(costingSettingsExpectedData.getDigitalFactory());
        softAssertions.assertThat(Integer.parseInt(costingServiceSettings.getAnnualVolume())).isEqualTo(costingSettingsExpectedData.getAnnualVolume());
        softAssertions.assertThat(Integer.parseInt(costingServiceSettings.getBatchSize())).isEqualTo(costingSettingsExpectedData.getBatchSize());
    }

    @After
    public void cleanup() {
        softAssertions.assertAll();
    }
}

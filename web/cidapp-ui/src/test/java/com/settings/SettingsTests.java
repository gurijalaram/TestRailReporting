package com.settings;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;

import com.apriori.apibase.utils.AfterTestUtil;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.settings.DisplayPreferencesPage;
import com.apriori.pageobjects.pages.settings.ProductionDefaultsPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ColourEnum;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.MetricEnum;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SanityTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class SettingsTests extends TestBase {
    File resourceFile;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private DisplayPreferencesPage displayPreferencesPage;
    private EvaluatePage evaluatePage;
    private ProductionDefaultsPage productionDefaultPage;
    private UserCredentials currentUser;

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class, SanityTests.class})
    @Test
    @TestRail(testCaseId = {"1609", "276"})
    @Description("User can change the default Production Defaults")
    public void changeProductionDefaults() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        productionDefaultPage = loginPage.login(currentUser)

            .openSettings()
            .goToProductionTab()
            .inputScenarioName("MP Auto Test")
            .selectProcessGroup(ProcessGroupEnum.ROTO_BLOW_MOLDING)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_EASTERN_EUROPE)
            .openMaterialSelectorTable()
            .selectMaterial("ABS, Plating")
            .submit(ProductionDefaultsPage.class)
            .inputAnnualVolume("3000")
            .inputYears("7")
            .inputBatchSize("50");
        new DisplayPreferencesPage(driver).submit(ExplorePage.class);

        explorePage = new ExplorePage(driver);
        productionDefaultPage = explorePage.openSettings()
            .goToProductionTab();

        assertThat(productionDefaultPage.getScenarioName(), is("MP Auto Test"));
        assertThat(productionDefaultPage.getSelectedProcessGroup(ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup()), is(true));
        assertThat(productionDefaultPage.getSelectedVPE(DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory()), is(true));
        assertThat(productionDefaultPage.getSelectedCatalog(DigitalFactoryEnum.APRIORI_EASTERN_EUROPE.getDigitalFactory()), is(true));
        assertThat(productionDefaultPage.getSelectedMaterial("ABS, Plating"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"274", "1609", "1602", "573", "599"})
    @Description("User can change the default Process group")
    public void defaultPG() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING);

        displayPreferencesPage = new DisplayPreferencesPage(driver);
        evaluatePage = displayPreferencesPage.submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .costScenario()
            .publishScenario()
            .publish(EvaluatePage.class);

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COSTING_FAILED), is(true));

    }

    @Test
    @TestRail(testCaseId = {"275"})
    @Description("User can change the default VPE")
    public void defaultVPE() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "partbody_2";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_MEXICO);

        displayPreferencesPage = new DisplayPreferencesPage(driver);
        evaluatePage = displayPreferencesPage.submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        assertThat(evaluatePage.getSelectedVPE(), is(DigitalFactoryEnum.APRIORI_MEXICO));
    }

    @Test
    @TestRail(testCaseId = {"278", "279", "561"})
    @Description("User can change the default Production Life")
    public void defaultProductionLife() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "partbody_2";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputAnnualVolume("9524")
            .inputYears("7");

        displayPreferencesPage = new DisplayPreferencesPage(driver);
        evaluatePage = displayPreferencesPage.submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        assertThat(evaluatePage.getAnnualVolume(), is("9,524"));
        assertThat(evaluatePage.getProductionLife(), is("7.00"));
    }

    @Test
    @TestRail(testCaseId = {"280", "281"})
    @Description("User can change the default Batch size when set to manual")
    public void defaultBatchSize() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentName = "Push Pin";
        final String batchSize = "46";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize(batchSize);

        displayPreferencesPage = new DisplayPreferencesPage(driver);
        evaluatePage = displayPreferencesPage.submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();
        //select secondary inputs

        assertThat(secondaryInputsPage.getBatchSize(), equalTo(batchSize));
    }

    @Test
    @TestRail(testCaseId = {"293"})
    @Description("User should be able to select a material catalogue from a different region than the VPE")
    public void differentMaterialCatalog() {

        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_GERMANY);

        displayPreferencesPage = new DisplayPreferencesPage(driver);
        productionDefaultPage = displayPreferencesPage.submit(ExplorePage.class)
            .openSettings()
            .goToProductionTab();

        assertThat(productionDefaultPage.getSelectedVPE(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory()), is(true));
        assertThat(productionDefaultPage.getSelectedCatalog(DigitalFactoryEnum.APRIORI_GERMANY.getDigitalFactory()), is(true));
    }

    @Category({CustomerSmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1609"})
    @Description("User can change the default selection colour")
    public void defaultColor() {

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .openSelectionTab()
            .setColour(ColourEnum.ELECTRIC_PURPLE.getColour());
        displayPreferencesPage = new DisplayPreferencesPage(driver);
        selectionDisplayPreferencesPage = displayPreferencesPage.save(ExplorePage.class)
            .openSettings()
            .openSelectionTab();

        assertThat(selectionDisplayPreferencesPage.getColour(), is(equalTo(ColourEnum.ELECTRIC_PURPLE.getColour())));
    }

    @Test
    @TestRail(testCaseId = {"277"})
    @Description("User can change the default Material")
    public void defaultMaterial() {

        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_PLASTIC)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_INDIA)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM)
            .openMaterialSelectorTable()
            .selectMaterial("HIPS Extrusion")
            .submit(DisplayPreferencesPage.class)
            .submit(ExplorePage.class)
            .openSettings()
            .goToProductionTab();

        assertThat(productionDefaultPage.getSelectedProcessGroup(ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup()), is(true));
        assertThat(productionDefaultPage.getSelectedVPE(DigitalFactoryEnum.APRIORI_INDIA.getDigitalFactory()), is(true));
        assertThat(productionDefaultPage.getSelectedCatalog(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM.getDigitalFactory()), is(true));
        assertThat(productionDefaultPage.getSelectedMaterial("HIPS Extrusion"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"271"})
    @Description("Have the users defaults automatically loaded for each login")
    public void logoutSettings() {
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_INDIA)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_MEXICO)
            .openMaterialSelectorTable()
            .selectMaterial("F-0005 Sponge")
            .submit(DisplayPreferencesPage.class)
            .submit(ExplorePage.class)
            .logout()
            .login(currentUser)
            .openSettings()
            .goToProductionTab();

        assertThat(productionDefaultPage.getSelectedProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup()), is(true));
        assertThat(productionDefaultPage.getSelectedVPE(DigitalFactoryEnum.APRIORI_INDIA.getDigitalFactory()), is(true));
        assertThat(productionDefaultPage.getSelectedCatalog(DigitalFactoryEnum.APRIORI_MEXICO.getDigitalFactory()), is(true));
        assertThat(productionDefaultPage.getSelectedMaterial("F-0005 Sponge"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"282"})
    @Description("Manual Batch Quantity cannot be zero")
    public void batchSize0() {

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectBatchManual()
            .enterBatchInput("0");

        warningPage = new DisplayPreferencesPage(driver).save(WarningPage.class);

        assertThat(warningPage.getWarningText(), Matchers.is(containsString("Some of the supplied inputs are invalid.")));
    }

    @Test
    @TestRail(testCaseId = {"300"})
    @Description("Manual Batch Quantity cannot be junk")
    public void batchSizeJunk() {

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectBatchManual()
            .enterBatchInput("JUNK");

        warningPage = new DisplayPreferencesPage(driver).save(WarningPage.class);

        assertThat(warningPage.getWarningText(), Matchers.is(containsString("Some of the supplied inputs are invalid.")));
    }

    @Test
    @TestRail(testCaseId = {"301", "302"})
    @Description("Manual Batch Quantity cannot be a decimal")
    public void batchSizeDecimal() {

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectBatchManual()
            .enterBatchInput("0.12.00");

        warningPage = new DisplayPreferencesPage(driver).save(WarningPage.class);

        assertThat(warningPage.getWarningText(), Matchers.is(containsString("Some of the supplied inputs are invalid.")));
    }

    @Test
    @TestRail(testCaseId = {"303"})
    @Description("Changes made on all tabs of the user preferences should be saved regardless of the tab that the save button was closed on")
    public void saveAllTabs() {

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .selectSystem(MetricEnum.ENGLISH.getMetricUnit())
            .goToProductionTab()
            .inputScenarioName("Save all tabs test")
            .inputAnnualVolume("295")
            .inputYears("7");

        new DisplayPreferencesPage(driver).openSelectionTab()
            .setColour(ColourEnum.SHAMROCK_GREEN.getColour());

        new DisplayPreferencesPage(driver).save(ExplorePage.class);

        explorePage = new ExplorePage(driver);
        displayPreferencesPage = explorePage.openSettings();
        assertThat(displayPreferencesPage.isSelectedMetricSystem(MetricEnum.ENGLISH.getMetricUnit()), is(true));

        productionDefaultPage = new DisplayPreferencesPage(driver).goToProductionTab();
        assertThat(productionDefaultPage.getScenarioName(), is("Save all tabs test"));

        selectionDisplayPreferencesPage = new DisplayPreferencesPage(driver).openSelectionTab();
        assertThat(selectionDisplayPreferencesPage.getColour(), is(equalTo(ColourEnum.SHAMROCK_GREEN.getColour())));
    }

    @Test
    @TestRail(testCaseId = {"295", "299"})
    @Description("Options should filter subsequent drop down options available")
    public void optionsFilter() {

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory());

        assertThat(productionDefaultPage.getListOfMaterials(), containsInAnyOrder("<No default specified>", "F-0005", "F-0005 Sponge", "FC-0205", "FD-0405", "FLC-4605", "FLN2-4405", "FN-0205"));
    }
}
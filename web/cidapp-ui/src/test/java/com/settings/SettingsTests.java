package com.settings;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

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
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.MetricEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.PublishPage;
import com.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.pageobjects.pages.evaluate.inputs.MoreInputsPage;
import com.pageobjects.pages.explore.ExplorePage;
import com.pageobjects.pages.login.CidLoginPage;
import com.pageobjects.pages.settings.ProductionDefaultPage;
import com.pageobjects.pages.settings.SelectionSettingsPage;
import com.pageobjects.pages.settings.SettingsPage;
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

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING);

        displayPreferencesPage = new DisplayPreferencesPage(driver);
        evaluatePage = displayPreferencesPage.submit(ExplorePage.class)
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .openJobQueue()
            .openScenarioLink(testScenarioName, "bracket_basic", "publish");

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_FAILURE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"275"})
    @Description("User can change the default VPE")
    public void defaultVPE() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "partbody_2.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_MEXICO.getDigitalFactory());

        displayPreferencesPage = new DisplayPreferencesPage(driver);
        evaluatePage = displayPreferencesPage.save(ExplorePage.class)
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getSelectedVPE(DigitalFactoryEnum.APRIORI_MEXICO.getDigitalFactory()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"278", "279", "561"})
    @Description("User can change the default Production Life")
    public void defaultProductionLife() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "partbody_2.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputAnnualVolume("9524")
            .inputYears("7");

        displayPreferencesPage = new DisplayPreferencesPage(driver);
        evaluatePage = displayPreferencesPage.save(ExplorePage.class)
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getAnnualVolume(), is("9,524"));
        assertThat(evaluatePage.getProductionLife(), containsString("7.00"));
    }

    @Test
    @TestRail(testCaseId = {"280", "281"})
    @Description("User can change the default Batch size when set to manual")
    public void defaultBatchSize() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String partName = "Push Pin.stp";
        final String batchSize = "46";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, partName);
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectBatchManual()
            .enterBatchInput(batchSize);

        displayPreferencesPage = new DisplayPreferencesPage(driver);
        moreInputsPage = displayPreferencesPage.save(ExplorePage.class)
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openMoreInputs();

        assertThat(moreInputsPage.getBatchSize(), equalTo(batchSize));
        assertThat(moreInputsPage.getCadFileName(), equalToIgnoringCase(partName));
    }

    @Test
    @TestRail(testCaseId = {"293"})
    @Description("User should be able to select a material catalogue from a different region than the VPE")
    public void differentMaterialCatalog() {

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .openProdDefaultTab()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_GERMANY.getDigitalFactory());
        displayPreferencesPage = new SettingsPage(driver);
        productionDefaultPage = displayPreferencesPage.save(ExplorePage.class)
            .openSettings()
            .openProdDefaultTab();

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
        displayPreferencesPage = new SettingsPage(driver);
        selectionSettingsPage = displayPreferencesPage.save(ExplorePage.class)
            .openSettings()
            .openSelectionTab();

        assertThat(selectionSettingsPage.getColour(), is(equalTo(ColourEnum.ELECTRIC_PURPLE.getColour())));
    }

    @Test
    @TestRail(testCaseId = {"277"})
    @Description("User can change the default Material")
    public void defaultMaterial() {

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .openProdDefaultTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup())
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_INDIA.getDigitalFactory())
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM.getDigitalFactory())
            .selectMaterial("HIPS Extrusion");

        displayPreferencesPage = new SettingsPage(driver);
        productionDefaultPage = displayPreferencesPage.save(ExplorePage.class)
            .openSettings()
            .openProdDefaultTab();

        assertThat(productionDefaultPage.getSelectedProcessGroup(ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup()), is(true));
        assertThat(productionDefaultPage.getSelectedVPE(DigitalFactoryEnum.APRIORI_INDIA.getDigitalFactory()), is(true));
        assertThat(productionDefaultPage.getSelectedCatalog(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM.getDigitalFactory()), is(true));
        assertThat(productionDefaultPage.getSelectedMaterial("HIPS Extrusion"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"271"})
    @Description("Have the users defaults automatically loaded for each login")
    public void logoutSettings() {

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .openProdDefaultTab()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_INDIA.getDigitalFactory())
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_MEXICO.getDigitalFactory())
            .selectMaterial("F-0005 Sponge");
        new SettingsPage(driver).save(ExplorePage.class);

        explorePage = new ExplorePage(driver);
        loginPage = explorePage.openAdminDropdown()
            .selectLogOut();

        loginPage = new CidLoginPage(driver);
        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .openProdDefaultTab();

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
            .openProdDefaultTab()
            .selectBatchManual()
            .enterBatchInput("0");

        warningPage = new SettingsPage(driver).save(WarningPage.class);

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
            .openProdDefaultTab()
            .selectBatchManual()
            .enterBatchInput("JUNK");

        warningPage = new SettingsPage(driver).save(WarningPage.class);

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
            .openProdDefaultTab()
            .selectBatchManual()
            .enterBatchInput("0.12.00");

        warningPage = new SettingsPage(driver).save(WarningPage.class);

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
            .openProdDefaultTab()
            .inputScenarioName("Save all tabs test")
            .inputAnnualVolume("295")
            .inputYears("7");

        new SettingsPage(driver).openSelectionTab()
            .setColour(ColourEnum.SHAMROCK_GREEN.getColour());

        new SettingsPage(driver).save(ExplorePage.class);

        explorePage = new ExplorePage(driver);
        displayPreferencesPage = explorePage.openSettings();
        assertThat(displayPreferencesPage.isSelectedMetricSystem(MetricEnum.ENGLISH.getMetricUnit()), is(true));

        productionDefaultPage = new SettingsPage(driver).openProdDefaultTab();
        assertThat(productionDefaultPage.getScenarioName(), is("Save all tabs test"));

        selectionSettingsPage = new SettingsPage(driver).openSelectionTab();
        assertThat(selectionSettingsPage.getColour(), is(equalTo(ColourEnum.SHAMROCK_GREEN.getColour())));
    }

    @Test
    @TestRail(testCaseId = {"295", "299"})
    @Description("Options should filter subsequent drop down options available")
    public void optionsFilter() {

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .openProdDefaultTab()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory());

        assertThat(productionDefaultPage.getListOfMaterials(), containsInAnyOrder("<No default specified>", "F-0005", "F-0005 Sponge", "FC-0205", "FD-0405", "FLC-4605", "FLN2-4405", "FN-0205"));
    }
}
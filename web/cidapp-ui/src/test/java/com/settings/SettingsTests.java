package com.settings;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.settings.DisplayPreferencesPage;
import com.apriori.pageobjects.pages.settings.ProductionDefaultsPage;
import com.apriori.pageobjects.pages.settings.SelectionPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColourEnum;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
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
    private SelectionPage selectionPage;

/*    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }*/

    @Category({SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1609", "276"})
    @Description("User can change the default Production Defaults")
    public void changeProductionDefaults() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)

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
            .inputBatchSize("50")
            .submit(ExplorePage.class);

        explorePage = new ExplorePage(driver);
        productionDefaultPage = explorePage.openSettings()
            .goToProductionTab();

        assertThat(productionDefaultPage.getScenarioName(), is("MP Auto Test"));
        assertThat(productionDefaultPage.getProcessGroup(), is(ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup()));
        assertThat(productionDefaultPage.getDigitalFactory(), is(DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory()));
        assertThat(productionDefaultPage.getMaterialCatalog(), is(DigitalFactoryEnum.APRIORI_EASTERN_EUROPE.getDigitalFactory()));
        assertThat(productionDefaultPage.getMaterial(), is("ABS, Plating"));
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
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING)
            .submit(ExplorePage.class)
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
            //.selectDigitalFactory(DigitalFactoryEnum.APRIORI_MEXICO)
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        //assertThat(evaluatePage.getDigitalFactory(), is(DigitalFactoryEnum.APRIORI_MEXICO));
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
            .inputYears("7")
            .submit(ExplorePage.class)
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
            .inputBatchSize(batchSize)
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();
        //select secondary inputs

        //assertThat(secondaryInputsPage.getBatchSize(), equalTo(batchSize));
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
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_GERMANY)
            .submit(ExplorePage.class)
            .openSettings()
            .goToProductionTab();

        assertThat(productionDefaultPage.getDigitalFactory(), is(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory()));
        assertThat(productionDefaultPage.getMaterialCatalog(), is(DigitalFactoryEnum.APRIORI_GERMANY.getDigitalFactory()));
    }

    @Test
    @TestRail(testCaseId = {"1609"})
    @Description("User can change the default selection colour")
    public void defaultColor() {
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        loginPage.login(currentUser)
            .openSettings()
            .goToSelectionTab()
            .selectColour(ColourEnum.PEAR)
            .submit(ExplorePage.class)
            .openSettings()
            .goToSelectionTab();

        assertThat(selectionPage.isColour(ColourEnum.PEAR), is(true));
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

        assertThat(productionDefaultPage.getProcessGroup(), is(ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup()));
        assertThat(productionDefaultPage.getDigitalFactory(), is(DigitalFactoryEnum.APRIORI_INDIA.getDigitalFactory()));
        assertThat(productionDefaultPage.getMaterialCatalog(), is(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM.getDigitalFactory()));
        assertThat(productionDefaultPage.getMaterial(), is("HIPS Extrusion"));
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

        assertThat(productionDefaultPage.getProcessGroup(), is(ProcessGroupEnum.POWDER_METAL.getProcessGroup()));
        assertThat(productionDefaultPage.getDigitalFactory(), is(DigitalFactoryEnum.APRIORI_INDIA.getDigitalFactory()));
        assertThat(productionDefaultPage.getMaterialCatalog(), is(DigitalFactoryEnum.APRIORI_MEXICO.getDigitalFactory()));
        assertThat(productionDefaultPage.getMaterial(), is("F-0005 Sponge"));
    }

    @Test
    @TestRail(testCaseId = {"282"})
    @Description("Manual Batch Quantity cannot be zero")
    public void batchSize0() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize("0");

        /*warningPage = new DisplayPreferencesPage(driver).save(WarningPage.class);

        assertThat(warningPage.getWarningText(), Matchers.is(containsString("Some of the supplied inputs are invalid.")));*/
    }

    @Test
    @TestRail(testCaseId = {"300"})
    @Description("Manual Batch Quantity cannot be junk")
    public void batchSizeJunk() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize("JUNK");

        /*warningPage = new DisplayPreferencesPage(driver).save(WarningPage.class);

        assertThat(warningPage.getWarningText(), Matchers.is(containsString("Some of the supplied inputs are invalid.")));*/
    }

    @Test
    @TestRail(testCaseId = {"301", "302"})
    @Description("Manual Batch Quantity cannot be a decimal")
    public void batchSizeDecimal() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize("0.12.00");

        /*warningPage = new DisplayPreferencesPage(driver).save(WarningPage.class);

        assertThat(warningPage.getWarningText(), Matchers.is(containsString("Some of the supplied inputs are invalid.")));*/
    }

    @Test
    @TestRail(testCaseId = {"303"})
    @Description("Changes made on all tabs of the user preferences should be saved regardless of the tab that the save button was closed on")
    public void saveAllTabs() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .setSystem("Imperial")
            .goToProductionTab()
            .inputScenarioName("Save all tabs test")
            .inputAnnualVolume("295")
            .inputYears("7");

        new DisplayPreferencesPage(driver).goToSelectionTab()
            .selectColour(ColourEnum.AMBER)
            .submit(ExplorePage.class)
            .openSettings();
        //assertThat(displayPreferencesPage.(MetricEnum.ENGLISH.getMetricUnit()), is(true));

        productionDefaultPage = new DisplayPreferencesPage(driver).goToProductionTab();
        assertThat(productionDefaultPage.getScenarioName(), is("Save all tabs test"));

        selectionPage = new DisplayPreferencesPage(driver).goToSelectionTab();
        assertThat(selectionPage.isColour(ColourEnum.AMBER), is(true));
    }

    @Test
    @TestRail(testCaseId = {"295", "299"})
    @Description("Options should filter subsequent drop down options available")
    public void optionsFilter() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_USA);

        //assertThat(productionDefaultPage.getListOfMaterials(), containsInAnyOrder("<No default specified>", "F-0005", "F-0005 Sponge", "FC-0205", "FD-0405", "FLC-4605", "FLN2-4405", "FN-0205"));
    }
}
package com.settings;

import static com.apriori.utils.enums.DigitalFactoryEnum.APRIORI_USA;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.MaterialSelectorPage;
import com.apriori.pageobjects.pages.evaluate.UpdateCadFilePage;
import com.apriori.pageobjects.pages.evaluate.inputs.AdvancedPage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.StockPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.ImportCadFilePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.settings.AssemblyDefaultsPage;
import com.apriori.pageobjects.pages.settings.DisplayPreferencesPage;
import com.apriori.pageobjects.pages.settings.ProductionDefaultsPage;
import com.apriori.pageobjects.pages.settings.SelectionPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.MaterialNameEnum;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UnitsEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColourEnum;
import com.utils.ColumnsEnum;
import com.utils.ComparisonCardEnum;
import com.utils.CurrencyEnum;
import com.utils.DecimalPlaceEnum;
import com.utils.LengthEnum;
import com.utils.MassEnum;
import com.utils.SortOrderEnum;
import com.utils.TimeEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ExtendedRegression;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;
import java.util.List;

public class SettingsTests extends TestBase {
    private File resourceFile;
    private File resourceFile2;
    private CidAppLoginPage loginPage;
    private DisplayPreferencesPage displayPreferencesPage;
    private EvaluatePage evaluatePage;
    private ExplorePage explorePage;
    private ProductionDefaultsPage productionDefaultPage;
    private AssemblyDefaultsPage assemblyDefaultsPage;
    private UserCredentials currentUser;
    private SelectionPage selectionPage;
    private ComponentInfoBuilder cidComponentItem;
    private AdvancedPage advancedPage;
    private ImportCadFilePage importCadFilePage;
    private UpdateCadFilePage updateCadFilePage;
    private MaterialSelectorPage materialSelectorPage;
    private StockPage stockPage;
    private ComparePage comparePage;
    private SoftAssertions softAssertions = new SoftAssertions();

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    @Category({SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"6283", "6637", "6275", "8883"})
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
            .selectMaterial(MaterialNameEnum.ABS_PLATING.getMaterialName())
            .submit(ProductionDefaultsPage.class)
            .inputAnnualVolume("3000")
            .inputYears("7")
            .inputBatchSize("50")
            .submit(ExplorePage.class)
            .openSettings()
            .goToProductionTab();

        softAssertions.assertThat(productionDefaultPage.getScenarioName()).isEqualTo("MP Auto Test");
        softAssertions.assertThat(productionDefaultPage.getProcessGroup()).isEqualTo(ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup());
        softAssertions.assertThat(productionDefaultPage.getDigitalFactory()).isEqualTo(DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterialCatalog()).isEqualTo(DigitalFactoryEnum.APRIORI_EASTERN_EUROPE.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterial()).isEqualTo("ABS, Plating");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6281", "5442"})
    @Description("User can change the default Process group")
    public void defaultPG() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING)
            .submit(ExplorePage.class)
            .uploadComponent(componentName, testScenarioName, resourceFile, currentUser);

        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
            .costScenario();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COSTING_FAILED), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6282"})
    @Description("User can change the default VPE")
    public void defaultVPE() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "partbody_2";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_MEXICO)
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        assertThat(evaluatePage.getDigitalFactory(), is(DigitalFactoryEnum.APRIORI_MEXICO.getDigitalFactory()));
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"6285", "6286", "5429"})
    @Description("User can change the default Production Life")
    public void defaultProductionLife() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "partbody_2";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputAnnualVolume("9524")
            .inputYears("7")
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getAnnualVolume()).isEqualTo("9524");
        softAssertions.assertThat(evaluatePage.getProductionLife()).isEqualTo("7");

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"6287", "6288"})
    @Description("User can change the default Batch size when set to manual")
    public void defaultBatchSize() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentName = "Push Pin";
        final String batchSize = "46";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        advancedPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize(batchSize)
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab();

        assertThat(advancedPage.getBatchSize(), is(equalTo(Integer.parseInt(batchSize))));
    }

    @Test
    @TestRail(testCaseId = {"6298"})
    @Description("User should be able to select a material catalogue from a different region than the VPE")
    public void differentMaterialCatalog() {

        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_GERMANY)
            .submit(ExplorePage.class)
            .openSettings()
            .goToProductionTab();

        softAssertions.assertThat(productionDefaultPage.getDigitalFactory()).isEqualTo(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterialCatalog()).isEqualTo(DigitalFactoryEnum.APRIORI_GERMANY.getDigitalFactory());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6280"})
    @Description("User can change the default selection colour")
    public void defaultColor() {
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        selectionPage = loginPage.login(currentUser)
            .openSettings()
            .goToSelectionTab()
            .selectColour(ColourEnum.PEAR)
            .submit(ExplorePage.class)
            .openSettings()
            .goToSelectionTab();

        assertThat(selectionPage.isColour(ColourEnum.PEAR), is(true));
    }

    @Test
    @TestRail(testCaseId = {"6284"})
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
            .selectMaterial(MaterialNameEnum.HIPS_EXTRUSION.getMaterialName())
            .submit(ProductionDefaultsPage.class)
            .submit(ExplorePage.class)
            .openSettings()
            .goToProductionTab();

        softAssertions.assertThat(productionDefaultPage.getProcessGroup()).isEqualTo(ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup());
        softAssertions.assertThat(productionDefaultPage.getDigitalFactory()).isEqualTo(DigitalFactoryEnum.APRIORI_INDIA.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterialCatalog()).isEqualTo(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterial()).isEqualTo("HIPS Extrusion");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6279", "6692", "6696"})
    @Description("Have the users defaults automatically loaded for each login")
    public void logoutSettings() {
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_INDIA)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_MEXICO)
            .openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.STEEL_F0005_SPONGE.getMaterialName())
            .submit(ProductionDefaultsPage.class)
            .submit(ExplorePage.class)
            .logout()
            .login(currentUser)
            .openSettings()
            .goToProductionTab();

        softAssertions.assertThat(productionDefaultPage.getProcessGroup()).isEqualTo(ProcessGroupEnum.POWDER_METAL.getProcessGroup());
        softAssertions.assertThat(productionDefaultPage.getDigitalFactory()).isEqualTo(DigitalFactoryEnum.APRIORI_INDIA.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterialCatalog()).isEqualTo(DigitalFactoryEnum.APRIORI_MEXICO.getDigitalFactory());
        softAssertions.assertThat(productionDefaultPage.getMaterial()).isEqualTo("F-0005 Sponge");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6289"})
    @Description("Manual Batch Quantity cannot be zero")
    public void batchSize0() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize("0")
            .submit();

        assertThat(productionDefaultPage.getErrorMessage(), is(equalTo("Must be greater than 0.")));
    }

    @Test
    @TestRail(testCaseId = {"3605"})
    @Description("Manual Batch Quantity cannot be junk")
    public void batchSizeJunk() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize("JUNK");

        assertThat(productionDefaultPage.getBatchSize(), is(emptyString()));
    }

    @Test
    @TestRail(testCaseId = {"6305", "6306", "6307"})
    @Description("Manual Batch Quantity cannot be a decimal")
    public void batchSizeDecimal() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        productionDefaultPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize("0.12.00");

        softAssertions.assertThat(productionDefaultPage.getErrorMessage()).isEqualTo("Must be an integer.");

        productionDefaultPage.inputAnnualVolume("0.12.01");
        softAssertions.assertThat(productionDefaultPage.getErrorMessage()).isEqualTo("Must be an integer.");

        productionDefaultPage.inputYears("0.12.02");
        softAssertions.assertThat(productionDefaultPage.getErrorMessage()).isEqualTo("Must be an integer.");

        productionDefaultPage.cancel(ExplorePage.class)
            .openSettings()
            .goToProductionTab()
            .inputBatchSize("this is txt");

        softAssertions.assertThat(productionDefaultPage.getBatchSize()).isEqualTo("");

        productionDefaultPage.inputAnnualVolume("this is txt");
        softAssertions.assertThat(productionDefaultPage.getAnnualVolume()).isEqualTo("");

        productionDefaultPage.inputYears("this is txt");
        softAssertions.assertThat(productionDefaultPage.getYears()).isEqualTo("");

        softAssertions.assertAll();
    }

    @Test
    @Issue("CID-1182")
    @TestRail(testCaseId = {"6308"})
    @Description("Changes made on all tabs of the user preferences should be saved regardless of the tab that the save button was closed on")
    public void saveAllTabs() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        displayPreferencesPage = loginPage.login(currentUser)
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .setSystem("Imperial")
            .goToProductionTab()
            .inputScenarioName("Save all tabs test")
            .inputAnnualVolume("295")
            .inputYears("7")
            .goToSelectionTab()
            .selectColour(ColourEnum.AMBER)
            .submit(ExplorePage.class)
            .openSettings();

        softAssertions.assertThat(displayPreferencesPage.isSystemChecked("Imperial")).isEqualTo(true);

        productionDefaultPage = displayPreferencesPage.goToProductionTab();

        softAssertions.assertThat(productionDefaultPage.getScenarioName()).isEqualTo("Save all tabs test");

        selectionPage = productionDefaultPage.goToSelectionTab();

        softAssertions.assertThat(selectionPage.isColour(ColourEnum.AMBER)).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6300", "6304"})
    @Description("Options should filter subsequent drop down options available")
    public void optionsFilter() {

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        materialSelectorPage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL)
            .selectMaterialCatalog(DigitalFactoryEnum.APRIORI_USA)
            .openMaterialSelectorTable();

        assertThat(materialSelectorPage.getListOfMaterials(), hasItems("F-0005", "F-0005 Sponge", "FC-0205", "FD-0405", "FLC-4605", "FLN2-4405", "FN-0205"));
    }

    @Test
    @TestRail(testCaseId = {"6277", "6291"})
    @Description("Successfully change the Currency")
    public void changeCurrency() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectCurrency(CurrencyEnum.EUR)
            .selectMass(MassEnum.GRAM)
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .selectDigitalFactory(APRIORI_USA)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getCostResultsString("Fully Burdened Cost")).contains("€");
        softAssertions.assertThat(evaluatePage.getFinishMass()).isEqualTo("5,309.46g");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6363", "5298"})
    @Description("Validate User Preferences are for single user only")
    public void settingsDifferentUsers() {

        UserCredentials user1 = UserUtil.getUser();
        UserCredentials user2 = UserUtil.getUser();
        new UserPreferencesUtil().resetSettings(user1);
        new UserPreferencesUtil().resetSettings(user2);

        loginPage = new CidAppLoginPage(driver);

        displayPreferencesPage = loginPage.login(user1)
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectLength(LengthEnum.MILLIMETER)
            .selectMass(MassEnum.GRAM)
            .selectTime(TimeEnum.MILLISECOND)
            .selectDecimalPlaces(DecimalPlaceEnum.TWO)
            .submit(ExplorePage.class)
            .logout()
            .login(user2)
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectLength(LengthEnum.METER)
            .selectMass(MassEnum.KILOGRAM)
            .selectTime(TimeEnum.MINUTE)
            .selectDecimalPlaces(DecimalPlaceEnum.FOUR)
            .submit(ExplorePage.class)
            .logout()
            .login(user1)
            .openSettings();

        softAssertions.assertThat(displayPreferencesPage.getLength()).isEqualTo("Millimeter");
        softAssertions.assertThat(displayPreferencesPage.getMass()).isEqualTo("Gram");
        softAssertions.assertThat(displayPreferencesPage.getTime()).isEqualTo("Millisecond");
        softAssertions.assertThat(displayPreferencesPage.getDecimalPlaces()).isEqualTo("2");

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"6368"})
    @Description("Validate when a user changes their unit settings comparison values update")
    public void customUnitsDisplayedInComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String componentName2 = "Push Pin";
        resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum, componentName2 + ".stp");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING)
            .costScenario()
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .createComparison()
            .selectManualComparison()
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectMass(MassEnum.GRAM)
            .selectTime(TimeEnum.MINUTE)
            .selectDecimalPlaces(DecimalPlaceEnum.FIVE)
            .submit(ComparePage.class);

        softAssertions.assertThat(comparePage.getOutput(componentName, scenarioName, ComparisonCardEnum.MATERIAL_FINISH_MASS)).isEqualTo("0.20809g");
        softAssertions.assertThat(comparePage.getOutput(componentName, scenarioName, ComparisonCardEnum.PROCESS_TOTAL_CYCLE_TIME)).isEqualTo("0.60952min");
        softAssertions.assertThat(comparePage.getOutput(componentName, scenarioName, ComparisonCardEnum.COST_TOTAL_CAPITAL_INVESTMENT)).isEqualTo("$10,918.70913");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6360", "6361", "6367"})
    @Description("Validate when a user uploads and costs a component their customer choice is shown")
    public void customUnitsAreDisplayedCorrectly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .selectDigitalFactory(APRIORI_USA)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getMaterialResult("Total Cycle Time")).isCloseTo(Double.valueOf(109.40), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getMaterialResult("Finish Mass")).isCloseTo(Double.valueOf(5.31), Offset.offset(15.0));

        stockPage = evaluatePage.openMaterialProcess()
            .openStockTab();

        softAssertions.assertThat(stockPage.getStockInfo("Height")).isEqualTo("154.00mm");

        evaluatePage = stockPage.closePanel()
            .openSettings()
            .selectUnits(UnitsEnum.CUSTOM)
            .selectLength(LengthEnum.METER)
            .selectMass(MassEnum.GRAM)
            .selectTime(TimeEnum.MINUTE)
            .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.getMaterialResult("Total Cycle Time")).isCloseTo(Double.valueOf(2.80), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getMaterialResult("Finish Mass")).isCloseTo(Double.valueOf(5309.46), Offset.offset(15.0));

        stockPage = evaluatePage.openMaterialProcess()
            .openStockTab();

        softAssertions.assertThat(stockPage.getStockInfo("Height")).isEqualTo("0.15m");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"8762"})
    @Description("Verify default Display Preferences")
    public void defaultDisplayPreferences() {

        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        displayPreferencesPage = loginPage.login(currentUser)
            .openSettings();

        softAssertions.assertThat(displayPreferencesPage.getUnits()).isEqualTo("MMKS");
        softAssertions.assertThat(displayPreferencesPage.isSystemChecked("Metric")).isTrue();
        softAssertions.assertThat(displayPreferencesPage.getLength()).isEqualTo("Millimeter");
        softAssertions.assertThat(displayPreferencesPage.getMass()).isEqualTo("Kilogram");
        softAssertions.assertThat(displayPreferencesPage.getTime()).isEqualTo("Second");
        softAssertions.assertThat(displayPreferencesPage.getDecimalPlaces()).isEqualTo("2");
        softAssertions.assertThat(displayPreferencesPage.getLanguage()).isEqualTo("English");
        softAssertions.assertThat(displayPreferencesPage.getCurrency()).isEqualTo("USD (United States Dollar)");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11885"})
    @Description("Validate that default scenario name can be adjusted through user preferences")
    public void changeTheDefaultScenarioName() {
        currentUser = UserUtil.getUser();
        String MYSCENARIONAME = "My Test Scenario Name";

        loginPage = new CidAppLoginPage(driver);
        importCadFilePage = loginPage.login(currentUser)
            .openSettings()
            .goToProductionTab()
            .inputScenarioName(MYSCENARIONAME)
            .submit(ExplorePage.class)
            .importCadFile();

        assertThat(importCadFilePage.getDefaultScenarioName(), is(MYSCENARIONAME));
    }

    @Test
    @TestRail(testCaseId = {"17154", "17155", "17156", "17157", "21547", "21548"})
    @Description("Assembly Strategy stuff")
    public void testAssemblyStrategyDropdown() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        String preferPublic = "Prefer Public Scenarios";
        String preferPrivate = "Prefer Private Scenarios";
        String preferMaturityAndStatus = "Prefer High Maturity and Complete Status";
        String componentName = "Hinge assembly";

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDASM");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        assemblyDefaultsPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .openSettings()
            .goToAssemblyDefaultsTab();

        softAssertions.assertThat(assemblyDefaultsPage.isAssemblyStrategyDropdownVisible()).as("Verify Dropdown is visible").isTrue();

        assemblyDefaultsPage.selectAssemblyStrategy(preferPublic);

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify Public is selected").isEqualTo(preferPublic);

        importCadFilePage = assemblyDefaultsPage.submit(EvaluatePage.class).importCadFile();

        softAssertions.assertThat(importCadFilePage.getAssociationAlert()).contains("Your current Assembly Association Strategy is: Prefer Public Scenarios. " +
            "This setting can be changed in User Preferences.");

        updateCadFilePage = importCadFilePage.closeDialog(EvaluatePage.class).clickActions().updateCadFile(resourceFile);

        softAssertions.assertThat(updateCadFilePage.getAssociationAlert()).contains("Your current Assembly Association Strategy is: Prefer Public Scenarios. " +
            "This setting can be changed in User Preferences.");

        updateCadFilePage.cancel(EvaluatePage.class)
            .openSettings()
            .goToAssemblyDefaultsTab();

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify strategy was saved").isEqualTo(preferPublic);

        assemblyDefaultsPage.selectAssemblyStrategy(preferMaturityAndStatus)
            .submit(EvaluatePage.class)
            .importCadFile();

        softAssertions.assertThat(importCadFilePage.getAssociationAlert()).contains("Your current Assembly Association Strategy is: Prefer High Maturity and Complete Status. " +
            "This setting can be changed in User Preferences.");

        importCadFilePage.closeDialog(EvaluatePage.class)
            .clickActions()
            .updateCadFile(resourceFile);

        softAssertions.assertThat(updateCadFilePage.getAssociationAlert()).contains("Your current Assembly Association Strategy is: Prefer High Maturity and Complete Status. " +
            "This setting can be changed in User Preferences.");

        updateCadFilePage.cancel(EvaluatePage.class)
            .openSettings()
            .goToAssemblyDefaultsTab();

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify that strategy was updated").isEqualTo(preferMaturityAndStatus);

        assemblyDefaultsPage.selectAssemblyStrategy(preferPrivate)
            .submit(EvaluatePage.class)
            .importCadFile();

        softAssertions.assertThat(importCadFilePage.getAssociationAlert()).contains("Your current Assembly Association Strategy is: Prefer Private Scenarios. " +
            "This setting can be changed in User Preferences.");

        importCadFilePage.closeDialog(EvaluatePage.class)
            .clickActions()
            .updateCadFile(resourceFile);

        softAssertions.assertThat(updateCadFilePage.getAssociationAlert()).contains("Your current Assembly Association Strategy is: Prefer Private Scenarios. " +
            "This setting can be changed in User Preferences.");

        updateCadFilePage.cancel(EvaluatePage.class)
            .openSettings()
            .goToAssemblyDefaultsTab();

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify that strategy was saved").isEqualTo(preferPrivate);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"17154", "17155", "17156", "17157", "21653", "21654", "21655", "21656"})
    @Description("Verify Assembly Strategy Dropdown and Description Cards")
    public void testAssemblyStrategyDropdownCards() {
        currentUser = UserUtil.getUser();

        String preferPublic = "Prefer Public Scenarios";
        String preferPrivate = "Prefer Private Scenarios";
        String preferMaturity = "Prefer High Maturity and Complete Status";
        String sameNameCriteria = "Same name as assembly";
        String mostRecentCriteria = "Most recent";
        String completeCriteria = "Complete status, Highest maturity";
        String maturityCriteria = "Highest maturity";

        String defaultMessage = "No Assembly Association Strategy has been selected. The default strategy, Prefer Private Scenarios, will be used.";

        List<String[]> cardDetails;

        loginPage = new CidAppLoginPage(driver);
        AssemblyDefaultsPage assemblyDefaultsPage = loginPage.login(currentUser)
            .openSettings()
            .goToAssemblyDefaultsTab();

        cardDetails = assemblyDefaultsPage.getAsmStrategyCardDetails();

        softAssertions.assertThat(assemblyDefaultsPage.isAssemblyStrategyDropdownVisible()).as("Verify Dropdown is visible").isTrue();

        softAssertions.assertThat(assemblyDefaultsPage.usingDefaultMessage.isDisplayed()).as("Using default message displayed").isTrue();
        softAssertions.assertThat(assemblyDefaultsPage.usingDefaultMessage.getText()).as("Default message presented").isEqualTo(defaultMessage);
        softAssertions.assertThat(assemblyDefaultsPage.getAsmStrategyCardCount()).as("Verify 4 cards displayed for default").isEqualTo(4);

        softAssertions.assertThat(cardDetails.get(0)[1]).as("Default selection is Prefer Private").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(0)[3]).as("Verify Default Criteria").isEqualTo(sameNameCriteria);

        softAssertions.assertThat(cardDetails.get(1)[1]).as("Default selection is Prefer Private").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(1)[3]).as("Verify Default Criteria").isEqualTo(mostRecentCriteria);

        softAssertions.assertThat(cardDetails.get(2)[1]).as("Default selection is Prefer Private").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(2)[3]).as("Verify Default Criteria").isEqualTo(sameNameCriteria);

        softAssertions.assertThat(cardDetails.get(3)[1]).as("Default selection is Prefer Private").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(3)[3]).as("Verify Default Criteria").isEqualTo(mostRecentCriteria);

        assemblyDefaultsPage.selectAssemblyStrategy(preferPublic);
        cardDetails = assemblyDefaultsPage.getAsmStrategyCardDetails();

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify Public is selected").isEqualTo(preferPublic);
        softAssertions.assertThat(assemblyDefaultsPage.getAsmStrategyCardCount()).as("Verify 4 cards displayed").isEqualTo(4);

        softAssertions.assertThat(cardDetails.get(0)[1]).as("Public Workspace checked initially").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(0)[3]).as("Verify Criteria").isEqualTo(sameNameCriteria);

        softAssertions.assertThat(cardDetails.get(1)[1]).as("Public Workspace checked initially").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(1)[3]).as("Verify Criteria").isEqualTo(mostRecentCriteria);

        softAssertions.assertThat(cardDetails.get(2)[1]).as("Private Workspace checked second").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(2)[3]).as("Verify Criteria").isEqualTo(sameNameCriteria);

        softAssertions.assertThat(cardDetails.get(3)[1]).as("Private Workspace checked second").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(3)[3]).as("Verify Criteria").isEqualTo(mostRecentCriteria);

        assemblyDefaultsPage = assemblyDefaultsPage.submit(ExplorePage.class)
            .openSettings()
            .goToAssemblyDefaultsTab();

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify strategy was saved").isEqualTo(preferPublic);

        assemblyDefaultsPage = assemblyDefaultsPage.selectAssemblyStrategy(preferPrivate);
        cardDetails = assemblyDefaultsPage.getAsmStrategyCardDetails();

        softAssertions.assertThat(assemblyDefaultsPage.getAsmStrategyCardCount()).as("Verify 4 cards displayed").isEqualTo(4);

        softAssertions.assertThat(cardDetails.get(0)[1]).as("Private Workspace checked initially").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(0)[3]).as("Verify Criteria").isEqualTo(sameNameCriteria);

        softAssertions.assertThat(cardDetails.get(1)[1]).as("Private Workspace checked initially").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(1)[3]).as("Verify Criteria").isEqualTo(mostRecentCriteria);

        softAssertions.assertThat(cardDetails.get(2)[1]).as("Public Workspace checked second").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(2)[3]).as("Verify Criteria").isEqualTo(sameNameCriteria);

        softAssertions.assertThat(cardDetails.get(3)[1]).as("Public Workspace checked second").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(3)[3]).as("Verify Criteria").isEqualTo(mostRecentCriteria);

        assemblyDefaultsPage.submit(ExplorePage.class)
            .openSettings()
            .goToAssemblyDefaultsTab();

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify that strategy was updated").isEqualTo(preferPrivate);

        assemblyDefaultsPage = assemblyDefaultsPage.selectAssemblyStrategy(preferMaturity);
        cardDetails = assemblyDefaultsPage.getAsmStrategyCardDetails();

        softAssertions.assertThat(assemblyDefaultsPage.getAsmStrategyCardCount()).as("Verify 6 cards displayed").isEqualTo(6);

        softAssertions.assertThat(cardDetails.get(0)[1]).as("Public Workspace checked initially").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(0)[3]).as("Verify Criteria").isEqualTo(completeCriteria);

        softAssertions.assertThat(cardDetails.get(1)[1]).as("Public Workspace checked initially").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(1)[3]).as("Verify Criteria").isEqualTo(maturityCriteria);

        softAssertions.assertThat(cardDetails.get(2)[1]).as("Public Workspace checked initially").isEqualTo("Public");
        softAssertions.assertThat(cardDetails.get(2)[3]).as("Verify Criteria").isEqualTo(sameNameCriteria);

        softAssertions.assertThat(cardDetails.get(3)[1]).as("Private Workspace checked second").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(3)[3]).as("Verify Criteria").isEqualTo(completeCriteria);

        softAssertions.assertThat(cardDetails.get(4)[1]).as("Private Workspace checked second").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(4)[3]).as("Verify Criteria").isEqualTo(maturityCriteria);

        softAssertions.assertThat(cardDetails.get(5)[1]).as("Private Workspace checked second").isEqualTo("Private");
        softAssertions.assertThat(cardDetails.get(5)[3]).as("Verify Criteria").isEqualTo(sameNameCriteria);

        assemblyDefaultsPage.submit(ExplorePage.class)
            .openSettings()
            .goToAssemblyDefaultsTab();

        softAssertions.assertThat(assemblyDefaultsPage.getCurrentAsmStrategy()).as("Verify that strategy was updated").isEqualTo(preferMaturity);

        softAssertions.assertAll();
    }
}

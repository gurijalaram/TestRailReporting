package com.apriori.pageobjects.utils;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.utils.enums.ColourEnum;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ToleranceEnum;
import com.apriori.utils.enums.UnitsEnum;

import org.openqa.selenium.WebDriver;

/**
 * @author mparker
 */

public class AfterTestUtil {

    private ExplorePage explorePage;

    private WebDriver driver;
    private PageUtils pageUtils;

    public AfterTestUtil(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
    }

    private final String NO_DEFAULT = "<No default specified>";

    /**
     * Resets the production default settings back to default
     */
    public void resetProductionDefaults() {
        driver.navigate().to("https://cid-te.awsdev.apriori.com/apriori/cost/");
        explorePage = new ExplorePage(driver);
        explorePage.openSettings()
            .openProdDefaultTab()
            .enterScenarioName("Initial")
            .selectProcessGroup(NO_DEFAULT)
            .selectVPE(NO_DEFAULT)
            .selectMaterialCatalog(NO_DEFAULT)
            .clearAnnualVolume()
            .clearProductionLife()
            .selectBatchAuto();
        new SettingsPage(driver).save(ExplorePage.class);
    }

    /**
     * Resets the display preference settings back to default
     */
    public void resetDisplayPreferences() {
        driver.navigate().to("https://cid-te.awsdev.apriori.com/apriori/cost/");
        explorePage = new ExplorePage(driver);
        explorePage.openSettings()
            .changeDisplayUnits(UnitsEnum.SYSTEM.getUnit())
            .changeCurrency(CurrencyEnum.USD.getCurrency());
        new SettingsPage(driver).save(ExplorePage.class);
    }

    /**
     * Resets the selection colour back to default
     */
    public void resetSelectionColour() {
        driver.navigate().to("https://cid-te.awsdev.apriori.com/apriori/cost/");
        explorePage = new ExplorePage(driver);
        explorePage.openSettings()
            .openSelectionTab()
            .setColour(ColourEnum.YELLOW.getColour());
        new SettingsPage(driver).save(ExplorePage.class);
    }

    /**
     * Resets the Tolerance settings back to default
     */
    public void resetToleranceSettings() {
        driver.navigate().to("https://cid-te.awsdev.apriori.com/apriori/cost/");
        explorePage = new ExplorePage(driver);
        explorePage.openSettings()
            .openTolerancesTab()
            .selectUseCADModel()
            .uncheckReplaceLessValuesButton();
        new AfterTestUtil(driver).resetSpecificTolValues();
    }

    /**
     * Resets the production default settings back to default
     */
    public void resetAllSettings() {
        driver.navigate().to("https://cid-te.awsdev.apriori.com/apriori/cost/");
        explorePage = new ExplorePage(driver);
        explorePage.openSettings()
            .changeDisplayUnits(UnitsEnum.SYSTEM.getUnit())
            .changeCurrency(CurrencyEnum.USD.getCurrency())
            .openSelectionTab()
            .setColour(ColourEnum.YELLOW.getColour());
        new SettingsPage(driver).openProdDefaultTab()
            .enterScenarioName("Initial")
            .selectProcessGroup(NO_DEFAULT)
            .selectVPE(NO_DEFAULT)
            .selectMaterialCatalog(NO_DEFAULT)
            .clearAnnualVolume()
            .clearProductionLife()
            .selectBatchAuto();
        new SettingsPage(driver).openTolerancesTab()
            .selectUseCADModel()
            .uncheckReplaceLessValuesButton();
        new AfterTestUtil(driver).resetSpecificTolValues();
    }

    /**
     * Clears any values in Use specific default values Tolerance PMI Policy
     */
    private void resetSpecificTolValues() {
        new ToleranceSettingsPage(driver).editValues()
            .setTolerance(ToleranceEnum.ROUGHNESSRA.getToleranceName(), "")
            .setTolerance(ToleranceEnum.ROUGHNESSRZ.getToleranceName(), "")
            .setTolerance(ToleranceEnum.DIAMTOLERANCE.getToleranceName(), "")
            .setTolerance(ToleranceEnum.TRUEPOSITION.getToleranceName(), "")
            .setTolerance(ToleranceEnum.BEND_ANGLE_TOLERANCE.getToleranceName(), "")
            .setTolerance(ToleranceEnum.CIRCULARITY.getToleranceName(), "")
            .setTolerance(ToleranceEnum.CONCENTRICITY.getToleranceName(), "")
            .setTolerance(ToleranceEnum.CYLINDRICITY.getToleranceName(), "")
            .setTolerance(ToleranceEnum.FLATNESS.getToleranceName(), "")
            .setTolerance(ToleranceEnum.PARALLELISM.getToleranceName(), "")
            .setTolerance(ToleranceEnum.PERPENDICULARITY.getToleranceName(), "")
            .setTolerance(ToleranceEnum.PROFILESURFACE.getToleranceName(), "")
            .setTolerance(ToleranceEnum.RUNOUT.getToleranceName(), "")
            .setTolerance(ToleranceEnum.TOTALRUNOUT.getToleranceName(), "")
            .setTolerance(ToleranceEnum.STRAIGHTNESS.getToleranceName(), "")
            .setTolerance(ToleranceEnum.SYMMETRY.getToleranceName(), "")
            .save(ToleranceSettingsPage.class);
        new ToleranceSettingsPage(driver).selectAssumeTolerance();

        new SettingsPage(driver).save(ExplorePage.class);
    }
}
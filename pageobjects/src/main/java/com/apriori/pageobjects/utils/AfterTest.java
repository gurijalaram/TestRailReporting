package com.apriori.pageobjects.utils;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.apriori.pageobjects.pages.evaluate.inputs.MoreInputsPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.pageobjects.pages.settings.ProductionDefaultPage;
import com.apriori.pageobjects.pages.settings.SelectionSettingsPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.utils.enums.ProcessGroupEnum;

import org.openqa.selenium.WebDriver;

/**
 * @author mparker
 */

public class AfterTest {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private SettingsPage settingsPage;
    private EvaluatePage evaluatePage;
    private ProductionDefaultPage productionDefaultPage;
    private MoreInputsPage moreInputsPage;
    private SelectionSettingsPage selectionSettingsPage;
    private WarningPage warningPage;


    private WebDriver driver;
    private PageUtils pageUtils;

    private final String NO_DEFAULT = "<No default specified>";

    /**
     * Resets the settings back to default
     */
    public void after() {
        goToExplore();
        explorePage = new ExplorePage(driver);
        explorePage.openSettings()
            .openProdDefaultTab()
            .enterScenarioName("Initial")
            .selectProcessGroup(ProcessGroupEnum.NO_DEFAULT.getProcessGroup())
            .selectVPE(NO_DEFAULT)
            .selectMaterialCatalog(NO_DEFAULT)
            .selectMaterial(NO_DEFAULT)
            .clearAnnualVolume()
            .clearProductionLife()
            .selectBatchAuto();
        new SettingsPage(driver).save(ExplorePage.class);
    }

    public ExplorePage goToExplore() {
        driver.navigate().to("https://cid-te.awsdev.apriori.com/apriori/cost/");
        return new ExplorePage(driver);
    }
}
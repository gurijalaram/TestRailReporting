package com.apriori.pageobjects.pages.evaluate.designguidance;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.DesignGuidanceController;
import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.help.HelpDocPage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class InvestigationPage extends LoadableComponent<InvestigationPage> {

    @FindBy(css = ".active [data-icon='eye']")
    private WebElement investigationTabActive;

    @FindBy(xpath = "//span[.='Topics']")
    private WebElement topicsHeader;

    @FindBy(css = ".investigation-overview .topic-description")
    private WebElement investigationDescription;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;
    private DesignGuidanceController designGuidanceController;

    public InvestigationPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        this.designGuidanceController = new DesignGuidanceController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(investigationTabActive);
        pageUtils.waitForElementToAppear(topicsHeader);
    }

    /**
     * Selects the topic
     *
     * @param topic - the topic
     * @return current page object
     */
    public InvestigationPage selectTopic(String topic) {
        By byTopic = designGuidanceController.getBy(topic);
        pageUtils.scrollWithJavaScript(pageUtils.waitForElementToAppear(byTopic), true).click();
        return this;
    }

    /**
     * Selects machining setup
     *
     * @param machiningSetup - the machining setup
     * @return current page object
     */
    public InvestigationPage selectMachiningSetup(String machiningSetup) {
        designGuidanceController.selectDropdown(machiningSetup);
        return this;
    }

    /**
     * Gets count column
     *
     * @return string
     */
    public int getGcdCount(String gcd) {
        return Integer.parseInt(designGuidanceController.getColumn(gcd, 1));
    }

    /**
     * Gets cycle time column
     *
     * @return string
     */
    public String getCycleTime(String gcd) {
        return designGuidanceController.getColumn(gcd, 1);
    }

    /**
     * Gets cycle time percentage column
     *
     * @return string
     */
    public String getCycleTimePercentage(String gcd) {
        return designGuidanceController.getColumn(gcd, 2);
    }

    /**
     * Selects gcd
     *
     * @param gcd - the gcd
     * @return current page object
     */
    public InvestigationPage selectGcd(String gcd) {
        designGuidanceController.selectGcd(gcd);
        return this;
    }

    /**
     * Gets the investigation description
     *
     * @return string
     */
    public String getInvestigationDescription() {
        return pageUtils.waitForElementToAppear(investigationDescription).getAttribute("textContent");
    }

    /**
     * Closes current panel
     *
     * @return new page object
     */
    public EvaluatePage closePanel() {
        return panelController.closePanel();
    }

    /**
     * Opens the help page
     *
     * @return new page object
     */
    public HelpDocPage openHelp() {
        return panelController.openHelp();
    }
}

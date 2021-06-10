package com.apriori.pageobjects.pages.evaluate.designguidance;

import com.apriori.pageobjects.common.PanelController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class InvestigationPage extends LoadableComponent<InvestigationPage> {

    @FindBy(xpath = "//span[.='Topics']")
    private WebElement topicsHeader;

    @FindBy(css = ".investigation-overview .topic-description")
    private WebElement investigationDescription;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PanelController panelController;

    public InvestigationPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.panelController = new PanelController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(topicsHeader);
    }

    /**
     * Selects the topic
     *
     * @param topic - the topic
     * @return current page object
     */
    public InvestigationPage selectTopic(String topic) {
        By byTopic = getBy(topic);
        pageUtils.scrollWithJavaScript(pageUtils.waitForElementToAppear(byTopic), true).click();
        return this;
    }

    /**
     * Gets element By
     *
     * @param element - the element
     * @return By
     */
    private By getBy(String element) {
        return By.xpath(String.format("//div[normalize-space(text())='%s']", element));
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

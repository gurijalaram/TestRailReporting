package com.pageobjects.pages.jobqueue;

import com.apriori.utils.PageUtils;

import com.pageobjects.pages.compare.ComparePage;
import com.pageobjects.pages.evaluate.EvaluatePage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class JobQueuePage extends LoadableComponent<JobQueuePage> {

    private final Logger logger = LoggerFactory.getLogger(JobQueuePage.class);

    @FindBy(css = ".table.table-striped")
    private WebElement jobQueueTable;

    @FindBy(css = "a[data-ap-comp='jobQueue'] .button-text")
    private WebElement jobQueueButton;

    @FindBy(xpath = "//div[@class='popover-content']//div[@class='gwt-Label']/ancestor::tr")
    private WebElement jobQueueFirstRow;

    private WebDriver driver;
    private PageUtils pageUtils;

    public JobQueuePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(jobQueueTable);
    }

    /**
     * Opens the scenario from the job queue
     *
     * @param scenarioName - the scenario name
     * @param partName     - the part name
     * @param jobType      - the job type
     * @return new page object
     */
    public EvaluatePage openScenarioLink(String scenarioName, String partName, String jobType) {
        By scenario = By.xpath("//div[.='" + StringUtils.capitalize(jobType) + "']/ancestor::tr//a[contains(@href,'#openFromQueue::sk,partState," + partName.toUpperCase() + "," + scenarioName + "')]");
        pageUtils.waitForElementAndClick(scenario);
        return new EvaluatePage(driver);
    }

    /**
     * Opens the comparison from the job queue
     *
     * @param scenarioName - the scenario name
     * @param comparisonName - the comparison name
     * @param jobType - the job type
     * @return new page object
     */
    public ComparePage openComparisonLink(String scenarioName, String comparisonName, String jobType) {
        By scenario = By.xpath("//div[.='" + StringUtils.capitalize(jobType) + "']/ancestor::tr//a[contains(@href,'#openFromQueue::sk,comparisonState," + comparisonName.toUpperCase() + "," + scenarioName + "')]");
        pageUtils.waitForElementAndClick(scenario);
        return new ComparePage(driver);
    }

    /**
     * Checks the job queue that the first job for the specified scenario is complete
     *
     * @param component    - the component name
     * @param scenarioName - the scenario name
     * @param jobType      - the job type
     * @param icon         - icon can be 'okay' or 'stop'
     * @return webelement
     */
    public JobQueuePage checkJobQueueActionStatus(String component, String scenarioName, String jobType, String icon) {
        statusIcon(component, scenarioName, jobType, icon);
        return this;
    }

    /**
     * Checks the most recent server processes in the job queue and return the title
     *
     * @param icon - icon can be 'okay' or 'stop'
     * @return webelement
     */
    public String getJobQueueIconMessage(String icon) {
        checkJobQueueRow(icon);
        return pageUtils.waitForElementToAppear(jobQueueFirstRow).getAttribute("innerHTML");
    }

    /**
     * Gets the first row in the job queue
     *
     * @param icon - the icon
     * @return current page object
     */
    public JobQueuePage checkJobQueueRow(String icon) {
        pageUtils.checkElementAttribute(jobQueueFirstRow, "innerHTML", "clock");
        pageUtils.checkElementAttribute(jobQueueFirstRow, "innerHTML", icon);
        return this;
    }

    /**
     * Closes the job queue
     *
     * @param className - the class the method should return
     * @param <T>       - the return type
     * @return new page object
     */
    public <T> T closeJobQueue(Class<T> className) {
        pageUtils.waitForElementAndClick(jobQueueButton);
        return PageFactory.initElements(driver, className);
    }

    private WebElement statusIcon(String component, String scenarioName, String jobType, String icon) {
        By statusIcon = By.xpath(String.format("//div[.='%s']/ancestor::tr//a[@title='%s']/ancestor::tr//div[.='%s']/ancestor::tr//img[@src='%s18.png']",
            component.toUpperCase(), scenarioName, StringUtils.capitalize(jobType), icon));
        return pageUtils.waitForElementToAppear(statusIcon);
    }
}
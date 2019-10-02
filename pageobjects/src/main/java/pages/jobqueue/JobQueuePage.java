package pages.jobqueue;

import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobQueuePage extends LoadableComponent<JobQueuePage> {

    private Logger logger = LoggerFactory.getLogger(JobQueuePage.class);

    @FindBy(css = ".table.table-striped")
    private WebElement jobQueueTable;

    @FindBy(css = "a[data-ap-comp='jobQueue']")
    private WebElement jobQueueButton;

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
     * Checks the job type in the job queue is complete
     *
     * @param scenarioName - the scenario name
     * @param jobType      - the jobtype
     */
    public JobQueuePage checkJobQueueActionComplete(String scenarioName, String jobType) {
        By status = By.xpath("//a[@title='" + scenarioName + "']/ancestor::tr//div[.='" + jobType + "']/ancestor::tr//img[@src='okay18.png']");
        pageUtils.waitForElementToAppear(status, 1);
        closeJobQueue();
        return this;
    }

    private void closeJobQueue() {
        while (pageUtils.isElementDisplayed(jobQueueTable)) {
            jobQueueButton.click();
        }
    }
}

package main.java.pages.evaluate.designguidance.investigation;

import main.java.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvestigationPage extends LoadableComponent<InvestigationPage> {

    private final Logger logger = LoggerFactory.getLogger(InvestigationPage.class);

    @FindBy(css = "div[data-ap-comp='dtcTopicTable']")
    private WebElement topicTable;

    @FindBy(css = ".gwt-ListBox")
    private WebElement threadableDropdown;

    @FindBy(css = "div[data-ap-comp='dtcInvestigationTableExt'] .edit-tolerances-btn")
    private WebElement editButton;

    @FindBy(css = "div[data-ap-comp='dtcTableExtArea'] div.v-grid-scroller-vertical")
    private WebElement threadScroller;

    private WebDriver driver;
    private PageUtils pageUtils;

    public InvestigationPage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(topicTable);
    }

    public ThreadingPage editThread(String thread) {
        selectGCD(thread).click();
        edit();
        return new ThreadingPage(driver);
    }

    /**
     * Selects investigation topic
     * @param topic - the investigation topic
     * @return current page object
     */
    public InvestigationPage selectInvestigationTopic(String topic) {
        By investigationTopic = By.xpath("//div[@data-ap-comp='dtcTopicTable']//div[.='" + topic + "']");
        pageUtils.waitForElementToAppear(investigationTopic).click();
        return this;
    }

    /**
     * Selects the gcd
     * @param thread - the thread
     * @return the thread as a webelement
     */
    private WebElement selectGCD(String thread) {
        By threadType = By.xpath("//div[@data-ap-comp='dtcTableExtArea']//div[contains(text(),'" + thread + "')]");
        return pageUtils.scrollToElement(threadType, threadScroller);
    }

    /**
     * Selects the edit button
     * @return new page object
     */
    private ThreadingPage edit() {
        editButton.click();
        return new ThreadingPage(driver);
    }
}
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

    public ThreadingPage editThread(String gcdType, String gcd) {
        selectGCDType(gcdType).click();
        selectGCD(gcd).click();
        selectEditButton();
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
     * Selects the gcd type
     * @param gcdType - the gcd type
     * @return the gcd type as a webelement
     */
    private WebElement selectGCDType(String gcdType) {
        By type = By.xpath("//div[@data-ap-comp='dtcTableExtArea']//div[contains(text(),'" + gcdType + "')]/ancestor::tr//label[@class]");
        return pageUtils.scrollToElement(type, threadScroller);
    }

    /**
     * Selects the gcd
     * @param gcd  - the gcd
     * @return the gcd as webelement
     */
    private WebElement selectGCD(String gcd) {
        By gcdElement = By.xpath("//div[@data-ap-comp='dtcTableExtArea']//div[contains(text(),'" + gcd + "')]/ancestor::tr[@class]");
        return pageUtils.scrollToElement(gcdElement, threadScroller);
    }

    /**
     * Selects the threadable dropdown
     * @param option - the dropdown in the option
     * @return current page object
     */
    public InvestigationPage selectThreadableGCD(String option)  {
        pageUtils.selectDropdownOption(threadableDropdown,option);
        return this;
    }

    /**
     * Selects the selectEditButton button
     * @return new page object
     */
    public ThreadingPage selectEditButton() {
        pageUtils.waitForElementToAppear(editButton);
        pageUtils.waitForElementToBeClickable(editButton).click();
        return new ThreadingPage(driver);
    }

    /**
     * Gets the button as a webelement
     * @return the button as webelement
     */
    public WebElement getEditButton() {
        return editButton;
    }
}
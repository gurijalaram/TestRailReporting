package com.apriori.pageobjects.pages.messages;

import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

@Slf4j
public class MessagesPage extends EagerPageComponent<MessagesPage> {

    @FindBy(xpath = "//h3[@data-testid='title']")
    private WebElement headerTitle;

    @FindBy(xpath = "//div[contains(@class,'MuiPaper-root MuiPaper-elevation MuiPaper-rounded')]")
    private WebElement allMessages;

    @FindBy(xpath = "//h3[contains(@data-testid,'created-by')]")
    private WebElement createdBy;

    @FindBy(xpath = "//div[contains(@data-testid,'date-from-now')]")
    private WebElement time;

    @FindBy(xpath = "//div[contains(@data-testid,'comment-content')]")
    private WebElement commentContent;

    public MessagesPage(WebDriver driver) {

        this(driver, log);
    }

    private WebDriver driver;

    public MessagesPage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Get Message page header title
     *
     * @return String
     */
    public String getMessagesHeaderTitle() {
        return getPageUtils().waitForElementToAppear(headerTitle).getText();
    }

    /**
     * Checks if Messages displayed on the page
     *
     * @return true/false
     */
    public boolean isMessagesDisplayed() {
        return getPageUtils().waitForElementAppear(allMessages).isDisplayed();
    }

    /**
     * Checks if created by displayed on discussions
     *
     * @return true/false
     */
    public boolean isCreateByDisplayed() {
        return getPageUtils().waitForElementAppear(createdBy).isDisplayed();
    }

    /**
     * Checks if time displayed on discussions
     *
     * @return true/false
     */
    public boolean isTimeDisplayed() {
        return getPageUtils().waitForElementAppear(time).isDisplayed();
    }

    /**
     * Checks if subject and attribute displayed
     *
     * @return true/false
     */
    public boolean isMessageElementsDisplayed(String element) {
        return getPageUtils().waitForElementToAppear(By.xpath("//h4[contains(text(),'" + element + "')]")).isDisplayed();
    }

    /**
     * Checks if comment content displayed on discussions
     *
     * @return true/false
     */
    public boolean isCommentContentDisplayed() {
        return getPageUtils().waitForElementAppear(commentContent).isDisplayed();
    }

    /**
     * Gets the subject and attribute values
     *
     * @return string
     */
    public String getMessageElementsValues(String value) {
        return getPageUtils().waitForElementToAppear(By.xpath("//h4[contains(text(),'" + value + "')]//..//following-sibling::div")).getAttribute("textContent");
    }

    /**
     * Gets the comment content
     *
     * @return string
     */
    public String getCommentContent() {
        return getPageUtils().waitForElementToAppear(commentContent).getAttribute("textContent");
    }
}
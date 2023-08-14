package com.apriori.pageobjects.messages;

import com.apriori.EagerPageComponent;
import com.apriori.pageobjects.partsandassembliesdetails.PartsAndAssembliesDetailsPage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

@Slf4j
public class MessagesPage extends EagerPageComponent<MessagesPage> {

    @FindBy(xpath = "//h3[@data-testid='title']")
    private WebElement headerTitle;

    @FindBy(xpath = "//div[contains(@id,'discussion')]")
    private WebElement allMessages;

    @FindBy(xpath = "//h3[contains(@data-testid,'created-by')]")
    private WebElement createdBy;

    @FindBy(xpath = "//div[contains(@data-testid,'date-from-now')]")
    private WebElement time;

    @FindBy(xpath = "//div[contains(@data-testid,'comment-content')]")
    private WebElement commentContent;

    @FindBy(xpath = "//button[@data-testid='toolbar-control-button-active']//p[@data-testid='toolbar-Unread']")
    private WebElement unreadFilterIcon;

    @FindBy(xpath = "//div[@data-testid='loader']")
    private WebElement spinner;

    @FindBy(xpath = "//h4[contains(@data-testid,'replies')]//span[contains(text(),'View 1 reply')]")
    private WebElement repliesLink;

    @FindBy(xpath = "//button[@data-testid='toolbar-control-button-inactive']//p[@data-testid='toolbar-Filter ']")
    private WebElement filtersOption;

    @FindBy(id = "popover-filter-control-messages")
    private WebElement filterModal;

    @FindBy(id = "add-condition-button-filter-control-messages")
    private WebElement addConditionLink;

    @FindBy(xpath = "//div[contains(@id,'filter-field-select')]")
    private WebElement filterField;

    @FindBy(xpath = "//div[contains(@id,'filter-condition-type')]")
    private WebElement filterConditionType;

    @FindBy(xpath = "//input[contains(@id,'filter-value')]")
    private WebElement filterValue;

    @FindBy(xpath = "//*[local-name()='svg' and @data-icon='times-circle']")
    private WebElement removeFilterIcon;

    @FindBy(xpath = "//li[@data-value='assignee.userIdentity']")
    private WebElement assignedToFilterFiled;

    @FindBy(xpath = "//li[@data-value='[IN]']")
    private WebElement isAnyOfFilterType;

    @FindBy(xpath = "//li[@data-value='mentionedUsers.userIdentity']")
    private WebElement mentionedUserFilterField;

    @FindBy(xpath = "//li[@data-value='[EQ]']")
    private WebElement isFilterType;

    @FindBy(xpath = "//div[contains(@id,'')]")
    private WebElement mentionedUserTag;

    @FindBy(xpath = "//li[@data-value='status']")
    private WebElement statusFilterField;

    @FindBy(xpath = "//button[@title='Open']//*[local-name()='svg']")
    private WebElement statusValueDropDown;

    @FindBy(xpath = "//p[contains(@data-testid,'show-status')]//*[local-name()='svg']")
    private WebElement resolveIcon;

    @FindBy(xpath = "//button[contains(@id,'more-options-menu-icon-button')]//*[local-name()='svg' and @data-icon='ellipsis-vertical']")
    private WebElement moreOptionMenu;

    @FindBy(xpath = "//li[@data-testid='menu-item-ASSIGN']")
    private WebElement assignToOption;

    @FindBy(xpath = "//div[contains(@id,'popover-select-control')]")
    private WebElement assignToUsersList;

    @FindBy(xpath = "//li[@data-testid='menu-item-UNASSIGN']")
    private WebElement unAssignToOption;

    @FindBy(xpath = "//button[@data-testid='toolbar-control-button-active']//p[@data-testid='toolbar-Filter (1)']")
    private WebElement addedFiltersOption;

    @FindBy(xpath = "//button[@data-testid='toolbar-control-button-inactive']//p[@data-testid='toolbar-Unread']")
    private WebElement readFilterIcon;

    @FindBy(xpath = "//button[@data-testid='toolbar-control-button-active']//p")
    private WebElement activeFilter;

    @FindBy(xpath = "//div[@id='user-discussion-msgs-container']//div[contains(@id,'discussion')][1]")
    private WebElement firstMessagePageDiscussion;

    public MessagesPage(WebDriver driver) {
        this(driver, log);
    }

    private WebDriver driver;

    public MessagesPage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
        this.waitForMessagePageLoad();
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

    /**
     * Checks if unread option displayed on discussions
     *
     * @return true/false
     */
    public boolean isUnreadOptionDisplayed() {
        return getPageUtils().waitForElementAppear(unreadFilterIcon).isDisplayed();
    }

    /**
     * clicks on unread option
     *
     * @return true/false
     */
    public MessagesPage clickOnUnread() {
        getPageUtils().waitForElementAndClick(unreadFilterIcon);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        return this;
    }

    /**
     * Checks if created discussion displayed on message page
     *
     * @return true/false
     */
    public boolean isMessagePageDiscussionDisplayed(String content) {
        return getPageUtils().waitForElementToAppear(By.xpath("//div[contains(@id,'discussion')]//div[contains(text(),'" + content + "')]")).isDisplayed();
    }

    /**
     * clicks on subject/attribute to open discussion
     *
     * @return new page object
     */
    public PartsAndAssembliesDetailsPage clickOnSubjectOrAttribute(String value) {
        getPageUtils().waitForElementAndClick(By.xpath("//h4[contains(text(),'" + value + "')]//..//following-sibling::div"));
        return new PartsAndAssembliesDetailsPage(getDriver());
    }

    /**
     * clicks on replies to open discussion
     *
     * @return new page object
     */
    public PartsAndAssembliesDetailsPage clickOnReplies() {
        getPageUtils().waitForElementAndClick(repliesLink);
        return new PartsAndAssembliesDetailsPage(getDriver());
    }

    /**
     * Method to wait message page loads
     */
    public void waitForMessagePageLoad() {
        getPageUtils().waitForElementToAppear(spinner);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
    }

    /**
     * Checks if filter option displayed
     *
     * @return true/false
     */
    public boolean isFilterOptionDisplayed() {
        return getPageUtils().waitForElementAppear(filtersOption).isDisplayed();
    }

    /**
     * clicks on filter option
     *
     * @return true/false
     */
    public MessagesPage clickOnFilter() {
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        getPageUtils().waitForElementAndClick(filtersOption);
        return this;
    }

    /**
     * Checks if filter modal displayed
     *
     * @return true/false
     */
    public boolean isFilterModalDisplayed() {
        return getPageUtils().waitForElementAppear(filterModal).isDisplayed();
    }

    /**
     * Checks if add condition option displayed
     *
     * @return true/false
     */
    public boolean isAddConditionOptionDisplayed() {
        return getPageUtils().waitForElementAppear(addConditionLink).isDisplayed();
    }

    /**
     * clicks on add condition link
     *
     * @return true/false
     */
    public MessagesPage clickOnAddCondition() {
        getPageUtils().waitForElementAndClick(addConditionLink);
        return this;
    }

    /**
     * Checks if filter field selector displayed
     *
     * @return true/false
     */
    public boolean isFilterFiledDisplayed() {
        return getPageUtils().waitForElementAppear(filterField).isDisplayed();
    }

    /**
     * Checks if filter type selector displayed
     *
     * @return true/false
     */
    public boolean isFilterConditionTypeDisplayed() {
        return getPageUtils().waitForElementAppear(filterConditionType).isDisplayed();
    }

    /**
     * Checks if filter value field displayed
     *
     * @return true/false
     */
    public boolean isFilterValueDisplayed() {
        return getPageUtils().waitForElementAppear(filterValue).isDisplayed();
    }

    /**
     * clicks on remove filter option
     *
     * @return true/false
     */
    public MessagesPage clickOnRemoveFilter() {
        getPageUtils().waitForElementAndClick(removeFilterIcon);
        return this;
    }

    /**
     * select assignee to filter
     *
     * @return current page object
     */
    public MessagesPage selectAssigneeToFilter(String assignee) {
        getPageUtils().waitForElementAndClick(filterField);
        getPageUtils().waitForElementAndClick(assignedToFilterFiled);
        getPageUtils().waitForElementAndClick(filterConditionType);
        getPageUtils().waitForElementAndClick(isAnyOfFilterType);
        getPageUtils().waitForElementAndClick(filterValue);
        getPageUtils().waitForElementToAppear(filterValue).sendKeys(assignee);
        getPageUtils().waitForElementAndClick(By.xpath("//*[contains(text(),'" + assignee + "')]"));
        return this;
    }

    /**
     * clicks on remove filter option
     *
     * @return true/false
     */
    public MessagesPage clickOnFilteredDiscussion() {
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        getPageUtils().waitForElementToAppear(allMessages);
        getPageUtils().javaScriptClick(firstMessagePageDiscussion);
        return this;
    }

    /**
     * get assigned state
     *
     * @return a String
     */
    public String getAssignedState() {
        return getPageUtils().waitForElementToAppear(firstMessagePageDiscussion).getAttribute("innerText");
    }

    /**
     * select mentioned user to filter
     *
     * @return current page object
     */
    public MessagesPage selectMentionedUserToFilter(String mentionedUser) {
        getPageUtils().waitForElementAndClick(filterField);
        getPageUtils().waitForElementAndClick(mentionedUserFilterField);
        getPageUtils().waitForElementAndClick(filterConditionType);
        getPageUtils().waitForElementAndClick(isFilterType);
        getPageUtils().waitForElementToAppear(filterValue).sendKeys(mentionedUser + Keys.ARROW_DOWN + Keys.ENTER);
        return this;
    }

    /**
     * Checks if mentioned user tag displayed
     *
     * @return true/false
     */
    public boolean isMentionedUserTagDisplayed(String mentionedUser) {
        return getPageUtils().isElementDisplayed(By.xpath("//div[contains(@id,'" + mentionedUser + "')]"));
    }

    /**
     * select status to filter
     *
     * @return current page object
     */
    public MessagesPage selectStatusToFilter(String status) {
        getPageUtils().waitForElementAndClick(filterField);
        getPageUtils().waitForElementAndClick(statusFilterField);
        getPageUtils().waitForElementAndClick(filterConditionType);
        getPageUtils().waitForElementAndClick(isAnyOfFilterType);
        getPageUtils().waitForElementToAppear(filterValue);
        getPageUtils().waitForElementAndClick(statusValueDropDown);
        getPageUtils().waitForElementAndClick(By.xpath("//span[contains(text(),'" + status + "')]"));
        return this;
    }

    /**
     * get resolve state
     *
     * @return a String
     */
    public String getResolveStatus() {
        return getPageUtils().waitForElementToAppear(resolveIcon).getAttribute("class");
    }

    /**
     * Checks if discussion-more options displayed
     *
     * @return true/false
     */
    public boolean isMoreOptionMenuDisplayed() {
        return getPageUtils().isElementDisplayed(moreOptionMenu);
    }

    /**
     * clicks on more option
     *
     * @return current page object
     */
    public MessagesPage clickOnMoreOptionMenu() {
        getPageUtils().waitForElementAndClick(moreOptionMenu);
        return this;
    }

    /**
     * clicks on assignTo option
     *
     * @return current page object
     */
    public MessagesPage clickOnAssignToOption() {
        getPageUtils().waitForElementToAppear(assignToOption);
        getPageUtils().javaScriptClick(assignToOption);
        return this;
    }

    /**
     * Checks if assign to users-list displayed
     *
     * @return true/false
     */
    public boolean isAssignToUserListDisplayed() {
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),2);
        return getPageUtils().isElementDisplayed(assignToUsersList);
    }

    /**
     * clicks on a participant
     *
     * @return current page object
     */
    public MessagesPage selectAUserToAssign(String participantName) {
        getPageUtils().waitForElementAndClick(By.xpath("//div[@role='button']//span[contains(text(),'" + participantName + "')]"));
        return this;
    }

    /**
     * clicks on Un-assignTo option
     *
     * @return current page object
     */
    public MessagesPage clickOnUnAssignToOption() {
        getPageUtils().waitForElementAndClick(unAssignToOption);
        return this;
    }

    /**
     * get discussion assigned state
     *
     * @return a String
     */
    public String getDiscussionAssignedState() {
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        return getPageUtils().waitForElementToAppear(firstMessagePageDiscussion).getAttribute("innerText");
    }

    /**
     * Checks if added filter options displayed
     *
     * @return true/false
     */
    public boolean isAddedFilterDisplayed() {
        return getPageUtils().isElementDisplayed(addedFiltersOption);
    }

    /**
     * reset to default configurations
     *
     * @return current page object
     */
    public MessagesPage resetToDefaultConfiguration() {
        getPageUtils().waitForElementAndClick(readFilterIcon);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        getPageUtils().waitForElementAndClick(activeFilter);
        clickOnRemoveFilter();
        return this;
    }

    /**
     * clicks on read option
     *
     * @return true/false
     */
    public MessagesPage clickOnRead() {
        getPageUtils().waitForElementAndClick(readFilterIcon);
        return this;
    }

    /**
     * clicks on filter option
     *
     * @return true/false
     */
    public MessagesPage clickOnActiveFilter() {
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        getPageUtils().waitForElementAndClick(activeFilter);
        return this;
    }
}
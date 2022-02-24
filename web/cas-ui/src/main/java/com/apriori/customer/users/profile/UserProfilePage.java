package com.apriori.customer.users.profile;

import com.apriori.utils.PageUtils;

import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserProfilePage extends LoadableComponent<UserProfilePage> {

    private static final Logger logger = LoggerFactory.getLogger(NewUserPage.class);

    @FindBy(xpath = "//button[.='Edit']")
    private WebElement editButton;

    @FindBy(xpath = "//button[.='Cancel']")
    private WebElement cancelButton;

    @FindBy(xpath = "//button[.='Save']")
    private WebElement saveButton;

    @FindBy(linkText = "< Back to User List Page")
    private WebElement backToUsersListPage;

    @FindBy(css = "[type='checkbox']")
    private WebElement statusCheckbox;

    private WebDriver driver;
    private PageUtils pageUtils;

    public UserProfilePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
        //Empty due to missed loading process
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(editButton);
    }

    /**
     * Edit customer info
     *
     * @return new page object
     */
    public UserProfilePage edit() {
        pageUtils.waitForElementAndClick(editButton);
        return this;
    }

    /**
     * Cancels customer info
     *
     * @return new page object
     */
    public UserProfilePage cancel() {
        pageUtils.waitForElementAndClick(cancelButton);
        return this;
    }

    /**
     * Saves user info
     *
     * @return new page object
     */
    public UserProfilePage save() {
        pageUtils.waitForElementAndClick(saveButton);
        return this;
    }

    /**
     * Opens customer staff page
     *
     * @return new page object
     */
    public <T> T backToUsersListPage(Class<T> klass) {
        pageUtils.waitForElementAndClick(backToUsersListPage);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Changes status of user
     *
     * @return this object
     */
    public UserProfilePage changeStatus() {
        pageUtils.waitForElementAndClick(statusCheckbox);
        return this;
    }

    /**
     * Gets user identity
     *
     * @return string
     */
    public String getUserIdentity() {
        return driver.findElement(By.xpath("//div[@class='text-overflow read-field read-field-identity']")).getAttribute("textContent");
    }

    /**
     * Checks if status checkbox is enabled
     *
     * @return true or false
     */
    public boolean isStatusCheckboxEditable() {
        return pageUtils.isElementEnabled(statusCheckbox);
    }

    /**
     * Gets the label for the given name.
     *
     * @param name - name of field
     * @return The label for the given name.
     */
    private WebElement getReadOnlyLabel(String name) {
        return pageUtils.waitForElementToAppear(By.cssSelector((String.format(".read-field-%s", name))));
    }

    /**
     * Gets the input for the given name.
     *
     * @param name - name of field
     * @return The input for the given name.
     */
    private WebElement getInput(String name) {
        if (name.equals("country-code") || name.equals("timezone")) {
            return pageUtils.waitForElementToAppear(By.cssSelector(String.format(".select-field-user-profile-%s", name)));
        } else {
            return pageUtils.waitForElementToAppear(By.xpath(String.format("//input[@name='%s']", name)));
        }
    }

    /**
     * Asserts that fields are read only
     *
     * @param namesOfFields - list of names of the fields
     * @param soft - soft assertions
     * @return - current page object
     */
    public UserProfilePage assertNonEditable(List<String> namesOfFields, SoftAssertions soft) {
        namesOfFields.forEach(name -> {
            soft.assertThat(getReadOnlyLabel(name))
                    .overridingErrorMessage(String.format("Expected field of %s to be read only.", name))
                    .isNotNull();
        });
        return this;
    }

    /**
     * Asserts that fields are editable
     *
     * @param nameOfFields - list of names of the fields
     * @param soft - soft assertions
     * @return - current page object
     */
    public UserProfilePage assertEditable(List<String> nameOfFields, SoftAssertions soft) {
        nameOfFields.forEach(name -> {
            soft.assertThat(getInput(name))
                    .overridingErrorMessage(String.format("Expected field of %s to be editable.", name))
                    .isNotNull();
        });
        return this;
    }

    /**
     * Checks if buttons are available
     *
     * @param soft - soft assertions
     * @param label - button label
     * @return - current page object
     */
    public UserProfilePage assertButtonAvailable(SoftAssertions soft, String label) {
        List<WebElement> elements = driver.findElements(By.xpath(String.format("//button[.='%s']", label)));
        soft.assertThat(elements.size())
                .overridingErrorMessage(String.format("Could not find the %s button", label))
                .isGreaterThan(0);
        return this;
    }

    /**
     * Checks if button is enabled
     *
     * @param button - name of button
     * @return - true or false
     */
    private boolean isButtonEnabled(WebElement button) {
        return button != null && button.isEnabled();
    }

    /**
     * Can click the save button.
     *
     * @return Boolean representing can click save button
     */
    public boolean canSave() {
        return isButtonEnabled(saveButton);
    }
}

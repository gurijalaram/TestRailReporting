package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishPage extends LoadableComponent<PublishPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(PublishPage.class);

    @FindBy(xpath = "//h5[.='Publish Scenario']")
    private WebElement headerDialog;

    @FindBy(css = "div[class='header-message'] p")
    private WebElement headerMessage;

    @FindBy(css = "div[class='checkbox-icon']")
    private WebElement lockTickBox;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

    public PublishPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(headerDialog);
    }

    /**
     * Set dropdown for any field
     *
     * @param dropdown - the dropdown
     * @param value    - the value
     * @return current page object
     */
    public PublishPage setDropdown(String dropdown, String value) {
        By theDropdown = By.xpath(String.format("//label[.='%s']/following-sibling::div[contains(@class,'apriori-select form-control')]", dropdown));
        pageUtils.waitForElementAndClick(theDropdown);
        By theValue = By.xpath(String.format("//button[.='%s']", value));
        pageUtils.scrollWithJavaScript(driver.findElement(theValue), true).click();
        return this;
    }

    /**
     * Click locked tick box
     * @return current page object
     */
    public PublishPage lock() {
        pageUtils.waitForElementToAppear(lockTickBox).click();
        return this;
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }

    /**
     * Select the publish button
     *
     * @return generic page object
     */
    public <T> T publish(Class<T> klass) {
        return modalDialogController.publish(klass);
    }
}

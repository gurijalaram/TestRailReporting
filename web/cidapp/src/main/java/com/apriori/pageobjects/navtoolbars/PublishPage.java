package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishPage extends LoadableComponent {

    private final Logger LOGGER = LoggerFactory.getLogger(PublishPage.class);

    @FindBy(css = "div[class='header-message'] p")
    private WebElement headerMessage;

    @FindBy(xpath = "//label[.='Status']/following-sibling::div[contains(@class,'apriori-select form-control')]")
    private WebElement statusDropdown;

    @FindBy(xpath = "//label[.='Cost Maturity']/following-sibling::div[contains(@class,'apriori-select form-control')]")
    private WebElement costMaturityDropdown;

    @FindBy(xpath = "//label[.='Assignee']/following-sibling::div[contains(@class,'apriori-select form-control')]")
    private WebElement assigneeDropdown;

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

    }

    public String getScenarioInfo() {
        return pageUtils.waitForElementAppear(headerMessage).getAttribute("textContent");
    }

    public PublishPage selectStatus(String status) {
        return this;
    }

    public PublishPage selectCostMaturity(String costMaturity) {
        return this;
    }

    public PublishPage selectAssignee(String assignee) {
        return this;
    }

    public PublishPage lock() {
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

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

public class DeletePage extends LoadableComponent<DeletePage> {
    private static final Logger logger = LoggerFactory.getLogger(DeletePage.class);

    @FindBy(xpath = "//h5[.='Delete']")
    private WebElement deleteHeader;

    @FindBy(css = ".delete-confirmation-form")
    private WebElement deleteText;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

    public DeletePage(WebDriver driver) {
        this.driver = driver;
        this.modalDialogController = new ModalDialogController(driver);
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(deleteHeader);
    }

    /**
     * Gets scenario name in delete text
     *
     * @return string
     */
    public String getScenarioName() {
        return pageUtils.waitForElementAppear(deleteText).getAttribute("textContent");
    }

    /**
     * Selects the submit button
     *
     * @return generic page object
     */
    public <T> T submit(Class<T> klass) {
        return modalDialogController.submit(klass);
    }

    /**
     * Select the cancel button
     *
     * @return generic page object
     */
    public <T> T cancel(Class<T> klass) {
        return modalDialogController.cancel(klass);
    }
}

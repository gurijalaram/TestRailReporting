package com.apriori.pageobjects.pages.explore;

import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

/**
 * @author cfrith
 */

@Slf4j
public class ImportCadFilePage extends LoadableComponent<ImportCadFilePage> {

    @FindBy(xpath = "//button[.='Close']")
    private WebElement closeButton;

    @FindBy(css = ".modal-body .message")
    private WebElement messageText;

    private WebDriver driver;
    private PageUtils pageUtils;
    private ModalDialogController modalDialogController;

    public ImportCadFilePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
        //Don't really need to do anything here
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(messageText);
    }

    /**
     * Close the import modal dialog
     *
     * @param klass - the class
     * @param <T>   - the generic page object
     * @return generic page object
     */
    public <T> T close(Class<T> klass) {
        pageUtils.waitForElementAndClick(closeButton);
        return PageFactory.initElements(driver, klass);
    }
}

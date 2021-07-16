package com.apriori.pageobjects.pages.evaluate.inputs;

import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.InputsController;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class CustomAttributesPage extends LoadableComponent<CustomAttributesPage> {

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Custom Attributes']")
    private WebElement customAttributesTab;

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Secondary']")
    private WebElement secondaryTab;

    @FindBy(xpath = "//div[@class='tabbed-layout scenario-inputs']//button[.='Primary']")
    private WebElement primaryTab;

    private WebDriver driver;
    private PageUtils pageUtils;
    private InputsController inputsController;
    private ModalDialogController modalDialogController;

    public CustomAttributesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        this.inputsController = new InputsController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("Custom Attributes was not selected", customAttributesTab.getAttribute("class").contains("active"));
    }

    /**
     * Opens primary tab
     *
     * @return new page object
     */
    public EvaluatePage goToPrimaryTab() {
        pageUtils.waitForElementAndClick(primaryTab);
        return new EvaluatePage(driver);
    }

    /**
     * Opens secondary tab
     *
     * @return new page object
     */
    public SecondaryInputsPage goToSecondaryTab() {
        pageUtils.waitForElementAndClick(secondaryTab);
        return new SecondaryInputsPage(driver);
    }
}

package com.apriori.pageobjects.navtoolbars;

import com.apriori.PageUtils;
import com.apriori.pageobjects.common.ModalDialogController;
import com.apriori.pageobjects.evaluate.EvaluatePage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class CostHistoryPage extends LoadableComponent<CostHistoryPage> {
    @FindBy(css = "h2")
    private WebElement header;

    @FindBy(css = "h2 button")
    private WebElement close;

    private PageUtils pageUtils;
    private WebDriver driver;
    private ModalDialogController modalDialogController;

    public CostHistoryPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.modalDialogController = new ModalDialogController(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(header);
    }

    /**
     * Click the Close button
     *
     * @return - EvaluatePage PO
     */
    public EvaluatePage close() {
        pageUtils.waitForElementAndClick(close);
        return new EvaluatePage(driver);
    }
}

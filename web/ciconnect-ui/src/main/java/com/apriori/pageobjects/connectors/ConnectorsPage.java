package com.apriori.pageobjects.connectors;

import com.apriori.pageobjects.CICBasePage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

/**
 * Connectors page
 */
public class ConnectorsPage extends CICBasePage {
    private static final Logger logger = LoggerFactory.getLogger(ConnectorsPage.class);

    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-44-grid-advanced > div.objbox > table > tbody")
    private WebElement connectorListTable;

    @FindBy(css = "div.xhdr td:nth-of-type(4)")
    private WebElement nameHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(5)")
    private WebElement descriptionHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(6)")
    private WebElement typeHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(8)")
    private WebElement connectionStatusHeader;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-29 > button")
    private WebElement editConnectorBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-30 > button")
    private WebElement deleteConnectorBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-28 > button")
    private WebElement newConnectorBtn;

    @FindBy(css = "div#root_pagemashupcontainer-1_button-31 > button")
    private WebElement refreshConnectorStatusBtn;

    @FindBy(css = "div[id='root_pagemashupcontainer-1_label-8'] > span")
    private WebElement connectorsLabel;


    public ConnectorsPage(WebDriver driver) {
        super(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() {
        pageUtils.waitForElementToAppear(newConnectorBtn);
    }


    /**
     * Select workflow in table
     *
     * @param connectorName - name of workflow to select
     * @return new Schedule page object
     */
    public ConnectorsPage selectConnector(String connectorName) {
        pageUtils.waitForElementAppear(connectorListTable);
        tableUtils.selectRowByName(connectorListTable, connectorName, 4);
        pageUtils.waitFor(Constants.DEFAULT_WAIT);
        return new ConnectorsPage(driver);
    }

    /**
     * Get connectors page label text
     *
     * @return String
     */
    public String getConnectorText() {
        pageUtils.waitForElementToAppear(connectorsLabel);
        return connectorsLabel.getText();
    }

    /**
     * Get connectors page Edit button
     *
     * @return WebElement
     */
    public WebElement getEditConnectorBtn() {
        return editConnectorBtn;
    }

    /**
     * Get connectors page Delete button
     *
     * @return WebElement
     */
    public WebElement getDeleteConnectorBtn() {
        return deleteConnectorBtn;
    }

    /**
     * Get connectors page New button
     *
     * @return WebElement
     */
    public WebElement getNewConnectorBtn() {
        return newConnectorBtn;
    }

    /**
     * Get connectors page refresh button
     *
     * @return WebElement
     */
    public WebElement getRefreshConnectorStatusBtn() {
        return refreshConnectorStatusBtn;
    }

}

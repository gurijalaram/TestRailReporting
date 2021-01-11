package com.apriori.connectors;

import com.apriori.header.PageHeader;
import com.apriori.utils.PageUtils;
import com.apriori.workflows.Schedule;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author kpatel
 */
public class ConnectorList extends LoadableComponent<ConnectorList> {

    private final Logger LOGGER = LoggerFactory.getLogger(ConnectorList.class);

    @FindBy(css = "div.objbox tr")
    private List<WebElement> connectorListTable;

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

    private WebDriver driver;
    private PageUtils pageUtils;
    private PageHeader pageHeader;

    public ConnectorList(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.pageHeader = new PageHeader(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
     * click Workflows tab
     *
     * @return new generic schedule page object from workflow tab
     */
    public Schedule clickWorkflowMenu() {
        return pageHeader.clickWorkflowMenu();
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
}

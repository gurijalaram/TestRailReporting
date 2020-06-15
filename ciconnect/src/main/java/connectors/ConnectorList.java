package connectors;

import com.apriori.utils.PageUtils;

import header.GenericHeader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author kpatel
 */
public class ConnectorList extends GenericHeader {

    private final Logger logger = LoggerFactory.getLogger(ConnectorList.class);

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

    private WebDriver driver;
    private PageUtils pageUtils;

    public ConnectorList(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
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
}

package newcustomer;

import com.apriori.utils.PageUtils;

import customeradmin.NavToolbar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfrastructurePage extends LoadableComponent<InfrastructurePage> {

    private final Logger logger = LoggerFactory.getLogger(InfrastructurePage.class);

    @FindBy(xpath = "//div[contains(text(),'Please select infrastructure within the Tree View')]")
    private WebElement noContentMessage;

    private WebDriver driver;
    private PageUtils pageUtils;
    private NavToolbar navToolbar;

    public InfrastructurePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.navToolbar = new NavToolbar(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementAppear(noContentMessage);
    }

    /**
     * Selects the infrastructure dropdown
     *
     * @param infrastructures - the infrastructure
     * @return current page object
     */
    public InfrastructurePage selectInfrastructureDropdown(String infrastructures) {
        String[] listInfrastructure = infrastructures.split(",");

        for (String infrastructure : listInfrastructure) {
            By dropdown = By.xpath(String.format("//li[.='%s']//div[@class='rstm-toggle-icon-symbol']", infrastructures));
            pageUtils.waitForElementAndClick(dropdown);
        }
        return this;
    }

    /**
     * Selects the data
     * @param name - the name
     * @return current page object
     */
    public InfrastructurePage selectData(String name) {
        By data = By.xpath(String.format("//li[.='%s']", name));
        pageUtils.waitForElementAndClick(data);
        return this;
    }
}

package reports.pages.homepage;

import com.apriori.pageobjects.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage extends LoadableComponent<HomePage> {

    private final Logger logger = LoggerFactory.getLogger(HomePage.class);

    @FindBy(css = "button[aria-label='Create Data Sources']")
    private WebElement createButton;


    private WebDriver driver;
    private PageUtils pageUtils;

    public HomePage(WebDriver driver){
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
    protected void isLoaded() throws Error {

    }

    /**
     * Check if Create button is displayed
     *
     * @return Visibility of button
     */
    public boolean isCreateButtonDisplayed(){
        pageUtils.waitForElementToAppear(createButton);
        return createButton.isDisplayed();
    }
}

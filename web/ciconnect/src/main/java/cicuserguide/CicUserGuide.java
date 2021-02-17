package cicuserguide;

import com.apriori.header.PageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CicUserGuide  {
    private final Logger logger = LoggerFactory.getLogger(CicUserGuide.class);

    @FindBy(css = "div[id='wwpID0ELHA']")
    private WebElement userGuideTitle;

    private WebDriver driver;
    private PageUtils pageUtils;
    private PageHeader pageHeader;

    public CicUserGuide(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        this.pageHeader = new PageHeader(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        //this.get();
    }

    protected void load() {
    }

    protected void isLoaded() throws Error {

    }

    /**
     * Switch to second tab
     *
     * @return CicUserGuide Page
     */
    public CicUserGuide switchTab() {
        pageUtils.windowHandler(1);
        return new CicUserGuide(driver);
    }

    /**
     * Switch iFrame
     *
     * @param iframeId
     * @return CicUserGuide Page
     * @throws Exception
     */
    public CicUserGuide switchToIFrameUserGuide(String iframeId) throws Exception {
        pageHeader.switchToIFrameUserGuide(iframeId);
        return new CicUserGuide(driver);
    }

    /**
     * Get user guide title text
     *
     * @return String, title text
     */
    public String getUserGuideTitle() {
        return userGuideTitle.getText();
    }

    /**
     * Get tab two URL
     *
     * @return String
     */
    public String getURL() {
        return pageUtils.getTabTwoUrl();
    }
}

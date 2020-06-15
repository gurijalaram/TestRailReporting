import com.apriori.utils.PageUtils;
import com.apriori.utils.constants.Constants;

import header.GenericHeader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workflows.GenericWorkflow;

public class LoginPage extends GenericHeader {

    private final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private static String loginPageURL = Constants.cicURL;

    private WebDriver driver;
    private PageUtils pageUtils;
    protected String url;

    public LoginPage(WebDriver driver) {
        super(driver);
        login(driver);
    }

    public GenericWorkflow login(WebDriver driver) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        if (url == null || url.isEmpty()) {
            url = "https://" + Constants.CIC_USERNAME + ":" + Constants.CIC_PASSWORD + "@" + loginPageURL;
            driver.get(url);
        }
        logger.info("CURRENTLY ON INSTANCE: " + url);
        PageFactory.initElements(driver, this);
        this.get();
        return new GenericWorkflow(driver);
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
    }
}
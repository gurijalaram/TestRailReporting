package com.apriori.cic.ui.pageobjects.home.help;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import com.apriori.cic.ui.pageobjects.CICBasePage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class CicAbout extends CICBasePage {

    @FindBy(xpath = "//span[.='About']")
    private WebElement cicAboutLbl;

    public CicAbout(WebDriver driver) {
        super(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    protected void load() {
    }

    protected void isLoaded()  {

    }

    /**
     * get WebElement of statement from aPG about
     *
     * @return WebElement
     */
    public WebElement getCicAboutStatement() {
        pageUtils.waitForElementAppear(cicAboutLbl);
        return driver.findElement(with(By.xpath("//span")).above(By.xpath("//div/span[contains(text(), 'For more information visit')]")));
    }

    /**
     * Get Element terms of use statement
     *
     * @return WebElement
     */
    public WebElement getCicTermsOfUse() {
        return driver.findElement(with(By.xpath("//span")).below(By.xpath("//span[.='Terms of Use']")));
    }

    /**
     * Get WebElement of copyright text
     *
     * @return WebElement
     */
    public WebElement getCopyRightElement() {
        return driver.findElement(with(By.xpath("//span")).below(By.xpath("//div/span[contains(text(), 'For more information visit')]")));
    }
}

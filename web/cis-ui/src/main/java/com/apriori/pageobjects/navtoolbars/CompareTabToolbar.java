package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.pages.compare.ModifyComparisonPage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class CompareTabToolbar extends MainNavigationBar {

    @FindBy(css = "[id= qa-sub-header-modify-button]")
    private WebElement modifyButton;

    public CompareTabToolbar(WebDriver driver) {
        super(driver, log);
    }

    /**
     * Clicks the Modify button
     *
     * @return new page object
     */
    public ModifyComparisonPage clickModify() {
        getPageUtils().waitForElementAndClick(modifyButton);
        return new ModifyComparisonPage(getDriver());
    }
}

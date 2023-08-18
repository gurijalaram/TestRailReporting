package com.apriori.pageobjects.compare;

import com.apriori.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class ModifyComparisonPage extends EagerPageComponent<ModifyComparisonPage> {

    @FindBy(css = ".modal-header")
    private WebElement modalHeader;

    public ModifyComparisonPage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(modalHeader);
    }

    /**
     * Get the modal header text
     *
     * @return String
     */
    public String getModifyComparisonHeaderText() {
        return getPageUtils().waitForElementToAppear(modalHeader).getAttribute("textContent");
    }
}

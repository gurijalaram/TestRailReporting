package com.apriori.qa.ach.ui.pageobjects;

import com.apriori.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class SwitchDeploymentPopUpPage extends LoadableComponent<SwitchDeploymentPopUpPage> {

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(xpath = "//div[@class='apriori-select searchable switch-deployment-dialog-deployments css-1xzq4gn-container']")
    private WebElement deploymentSelector;

    @FindBy(css = "button[type='submit']")
    private WebElement deploymentsPopUpSubmit;


    public SwitchDeploymentPopUpPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        PageFactory.initElements(driver, this);
        this.get();
    }

    public CloudHomePage clickSubmitButton() {
        pageUtils.waitForElementAndClick(deploymentsPopUpSubmit);
        return new CloudHomePage(driver);
    }


    public SwitchDeploymentPopUpPage selectDeployment(final String customerDeploymentName) {
        pageUtils.waitForElementAndClick(
                By.xpath(String.format("//div[contains(@class, 'MuiDialogContent-root')]//div[@class='text-overflow option-content' and contains(text(), '%s')]",
                        customerDeploymentName))
        );
        return this;
    }

    public SwitchDeploymentPopUpPage clickDeploymentSelector() {
        pageUtils.waitForElementAndClick(deploymentSelector);
        return this;
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }
}

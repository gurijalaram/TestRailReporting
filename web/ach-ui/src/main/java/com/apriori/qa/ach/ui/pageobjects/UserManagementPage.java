package com.apriori.qa.ach.ui.pageobjects;

import com.apriori.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class UserManagementPage extends LoadableComponent<UserManagementPage> {

    @FindBy(css = "img[alt='Application Logo']")
    private WebElement logo;
    @FindBy(xpath = "//button[contains(@class,'add-user-button')]")
    private WebElement addUserButton;
    @FindBy(xpath = "//div[contains(@class,'searchable')]")
    private WebElement rolesDropDown;
    @FindBy(xpath = "//div[@class = 'enablements-additional-properties']")
    private WebElement additionalProperties;
    private PageUtils pageUtils;
    private WebDriver driver;

    public UserManagementPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(logo);
    }

    public UserManagementPage clickAdduser() {
        pageUtils.waitForElementAndClick(addUserButton);
        return this;
    }

    public List<String> clickDropDownAndGetRoles() {
        pageUtils.waitForElementAndClick(rolesDropDown);
        List<WebElement> listOfRoles = driver.findElements(By.xpath("//div[contains(@class,'option-display-name')]"));
        return listOfRoles.stream().map(i -> i.getText()).collect(Collectors.toList());
    }

    public List<String> getAdditionalProperties() {
        List<String> list = new LinkedList<>(Arrays.asList(additionalProperties.getText().split("\n")));
        list.remove(0);
        return list;
    }


}

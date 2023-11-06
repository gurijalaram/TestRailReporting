package com.apriori.web.app.util.login;

import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;
import java.util.stream.Collectors;

public class UserProfilePage extends LoadableComponent<UserProfilePage> {
    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(css = "div[class='avatar']")
    private WebElement avatar;

    public UserProfilePage(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(avatar);
    }
    /**
     * get all input fields from user profile for assertion
     *
     * @return list of strings - names of fields
     */

    public List<String> getAllInputFieldsName() {
        pageUtils.waitForElementsToAppear(By.xpath("//form/div/div"));
        List<WebElement> elements = driver.findElements(By.xpath("//form/div/div"));
        List<String> listOfElements = elements.stream().map(m -> m.getText())
            .collect(Collectors.toList());

        return cutOffStringsAfterNewLineChar(listOfElements);
    }

    /**
     * remove all unnecessary part of string after new line char
     *
     * @return list of strings - names of fields
     */
    private List<String> cutOffStringsAfterNewLineChar(List<String> list) {
        return list.stream().map(w -> w.split("")[0])
            .collect(Collectors.toList());
    }
}

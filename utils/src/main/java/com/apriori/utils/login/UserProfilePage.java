package com.apriori.utils.login;

import com.apriori.utils.PageUtils;

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
        List<WebElement> element = driver.findElements(By.xpath("//form/div/div"));
        List<String> list = element.stream().map(m -> m.getText())
            .collect(Collectors.toList());

        String str = list.get(10).split("\n")[0];
        return cutOffStringsAfterNewLineChar(list);
    }

    /**
     * remove all unnecessary part of string after new line char
     *
     * @return list of strings - names of fields
     */
    private List<String> cutOffStringsAfterNewLineChar(List<String> list) {
        return list.stream().map(w -> w.split("\n")[0])
            .collect(Collectors.toList());
    }
}

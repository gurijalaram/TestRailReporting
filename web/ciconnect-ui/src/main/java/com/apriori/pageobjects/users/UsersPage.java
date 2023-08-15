package com.apriori.pageobjects.users;

import com.apriori.pageobjects.CICBasePage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Users Page
 */
public class UsersPage extends CICBasePage {

    private static final Logger logger = LoggerFactory.getLogger(UsersPage.class);

    @FindBy(css = "div.objbox tr")
    private List<WebElement> userListTable;

    @FindBy(css = "div.xhdr td:nth-of-type(1)")
    private WebElement usernameHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(2)")
    private WebElement emailHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(3)")
    private WebElement fullNameHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(4)")
    private WebElement departmentHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(5)")
    private WebElement locationHeader;

    @FindBy(css = "div.xhdr td:nth-of-type(6)")
    private WebElement timezoneHeader;

    @FindBy(xpath = "//*[@id=\"root_pagemashupcontainer-1_label-8\"]/span")
    private WebElement usersLabel;

    @FindBy(css = "#root_menu-19 > li:nth-child(2) > table > tbody > tr > td > a > span")
    private WebElement userTab;

    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-44-grid-advanced > div.objbox > table > tbody")
    private WebElement userList;


    public UsersPage(WebDriver driver) {
        super(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() {
        pageUtils.waitForElementToAppear(usersLabel);
        pageUtils.waitForElementToAppear(usernameHeader);
    }

    /**
     * Get Users text
     *
     * @return String
     */
    public String getUsersText() {
        return usersLabel.getText();
    }

    /**
     * Validates the Schedule Workflow list is sorted alphabetically
     *
     * @return True if the workflow list is sorted alphabetical
     */
    public Map<String, List<String>> validateUsersSortedAlphabetical() {
        pageUtils.waitForElementToBeClickable(userList);
        List<String> users = new ArrayList<>();
        userList.findElements(By.tagName("tr > td[0]"))
            .stream()
            .skip(1)
            .forEach(name -> users.add(name.getText()));

        List<String> sortedUsers = new ArrayList<>();
        users.forEach(name -> sortedUsers.add(name));
        Collections.sort(sortedUsers);

        Map<String, List<String>> usrLists = new HashMap();
        usrLists.put("OriginalUsers", users);
        usrLists.put("SortedUsers", sortedUsers);
        return usrLists;
    }
}

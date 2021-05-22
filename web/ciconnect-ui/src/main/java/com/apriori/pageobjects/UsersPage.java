package com.apriori.pageobjects;

import com.apriori.utils.PageUtils;

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

public class UsersPage {
    private static final Logger logger = LoggerFactory.getLogger(UsersPage.class);

    @FindBy(css = "#root_menu-19 > li:nth-child(2) > table > tbody > tr > td > a > span")
    private WebElement userTab;
    @FindBy(css = "#root_pagemashupcontainer-1_gridadvanced-44-grid-advanced > div.objbox > table > tbody")
    private WebElement userList;

    private WebDriver driver;
    private PageUtils pageUtils;

    public UsersPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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

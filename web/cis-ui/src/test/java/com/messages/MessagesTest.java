package com.messages;


import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.messages.MessagesPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;


public class MessagesTest extends TestBase {

    public MessagesTest() {
        super();
    }

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private MessagesPage messagesPage;

    @Test
    @TestRail(testCaseId = {"13317","13318","13319","13554","13561"})
    @Description("Verify messages page navigation and view all messages on the page")
    public void testMessagePageContent() {
        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser());
        softAssertions.assertThat(leftHandNavigationBar.isMessagesLinkDisplayed()).isEqualTo(true);

        messagesPage = leftHandNavigationBar.clickMessages();

        softAssertions.assertThat(messagesPage.getMessagesHeaderTitle()).contains("All Messages");
        softAssertions.assertThat(messagesPage.isMessagesDisplayed()).isEqualTo(true);
        softAssertions.assertThat(messagesPage.isCreateByDisplayed()).isEqualTo(true);
        softAssertions.assertThat(messagesPage.isTimeDisplayed()).isEqualTo(true);
        softAssertions.assertThat(messagesPage.isMessageElementsDisplayed("Subject")).isEqualTo(true);
        softAssertions.assertThat(messagesPage.getMessageElementsValues("Subject")).isNotEmpty();
        softAssertions.assertThat(messagesPage.isMessageElementsDisplayed("Attribute")).isEqualTo(true);
        softAssertions.assertThat(messagesPage.getMessageElementsValues("Attribute")).isNotEmpty();
        softAssertions.assertThat(messagesPage.isCommentContentDisplayed()).isEqualTo(true);
        softAssertions.assertThat(messagesPage.getCommentContent()).isNotEmpty();

        softAssertions.assertAll();

    }
}



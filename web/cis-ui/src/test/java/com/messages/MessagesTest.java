package com.messages;


import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.messages.MessagesPage;
import com.apriori.pageobjects.pages.partsandassemblies.PartsAndAssembliesPage;
import com.apriori.pageobjects.pages.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;


public class MessagesTest extends TestBase {

    public MessagesTest() {
        super();
    }

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private MessagesPage messagesPage;
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;
    private PartsAndAssembliesPage partsAndAssembliesPage;
    private File resourceFile;
    private UserCredentials currentUser;

    @Test
    @TestRail(testCaseId = {"13317","13318","13319","13554","13561"})
    @Description("Verify messages page navigation and view all messages on the page")
    public void testMessagePageContent() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesDetailsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickPartsAndAssemblies()
                .sortDownCreatedAtField()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName)
                .clickOnComponentName(componentName)
                .clickDigitalFactoryMessageIcon()
                .addComment("New Discussion")
                .clickComment()
                .selectCreatedDiscussion()
                .addComment("New Reply")
                .clickComment()
                .selectCreatedDiscussion();

        LeftHandNavigationBar leftHandNavigationBar = new LeftHandNavigationBar(driver);

        messagesPage = leftHandNavigationBar.clickMessages()
                .clickOnUnread();

        SoftAssertions softAssertions = new SoftAssertions();

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

    @Test
    @TestRail(testCaseId = {"14759","16107","16108"})
    @Description("Open a discussion from subject/attribute/replies")
    public void testMessagePageDiscussions() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesDetailsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickPartsAndAssemblies()
                .sortDownCreatedAtField()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName)
                .clickOnComponentName(componentName)
                .clickDigitalFactoryMessageIcon()
                .addComment("New Discussion")
                .clickComment()
                .selectCreatedDiscussion()
                .addComment("New Reply")
                .clickComment()
                .selectCreatedDiscussion();

        LeftHandNavigationBar leftHandNavigationBar = new LeftHandNavigationBar(driver);

        messagesPage = leftHandNavigationBar.clickMessages();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(messagesPage.isUnreadOptionDisplayed()).isEqualTo(true);

        messagesPage.clickOnUnread();

        softAssertions.assertThat(messagesPage.isMessagePageDiscussionDisplayed("New Discussion")).isEqualTo(true);

        partsAndAssembliesDetailsPage = messagesPage.clickOnSubjectOrAttribute("Subject");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isEqualTo(true);

        messagesPage = leftHandNavigationBar.clickMessages()
                .clickOnUnread();

        partsAndAssembliesDetailsPage = messagesPage.clickOnReplies();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isEqualTo(true);

        messagesPage = leftHandNavigationBar.clickMessages()
                .clickOnUnread();

        partsAndAssembliesDetailsPage = messagesPage.clickOnSubjectOrAttribute("Attribute");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isEqualTo(true);

        softAssertions.assertAll();
    }
}



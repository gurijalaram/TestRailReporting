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
        SoftAssertions softAssertions = new SoftAssertions();

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser());

        softAssertions.assertThat(leftHandNavigationBar.isMessagesLinkDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage = leftHandNavigationBar.uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
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

        messagesPage = leftHandNavigationBar.clickMessages()
                .clickOnUnread();

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
    @TestRail(testCaseId = {"14741","14748","14750","14758","14759","16107","16108"})
    @Description("Open a discussion from subject/attribute/replies")
    public void testMessagePageDiscussions() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(currentUser);
        partsAndAssembliesDetailsPage = leftHandNavigationBar.uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
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

        messagesPage = leftHandNavigationBar.clickMessages();

        softAssertions.assertThat(messagesPage.isFilterOptionDisplayed()).isEqualTo(true);

        messagesPage.clickOnFilter();

        softAssertions.assertThat(messagesPage.isFilterModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(messagesPage.isAddConditionOptionDisplayed()).isEqualTo(true);

        messagesPage.clickOnAddCondition();

        softAssertions.assertThat(messagesPage.isFilterFiledDisplayed()).isEqualTo(true);
        softAssertions.assertThat(messagesPage.isFilterConditionTypeDisplayed()).isEqualTo(true);
        softAssertions.assertThat(messagesPage.isFilterValueDisplayed()).isEqualTo(true);

        messagesPage.clickOnRemoveFilter();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14755", "14757"})
    @Description("Verify that user can filter discussions by assigned user")
    public void testFilterMessagesByAssignedUser() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(currentUser);
        partsAndAssembliesDetailsPage = leftHandNavigationBar.uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
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
                .shareScenario()
                .selectAUser("qa-automation-22@apriori.com")
                .clickOnInvite()
                .clickOnCreatedDiscussion()
                .clickMoreOption()
                .clickAssignToOption()
                .selectAParticipant("QA Automation Account 22");

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAssignedState()).contains("QA Automation Account 22");

        messagesPage = leftHandNavigationBar.clickMessages()
                .clickOnUnread()
                .clickOnFilter()
                .clickOnAddCondition()
                .selectAssigneeToFilter("QA Automation Account 22")
                .clickOnFilteredDiscussion();

        softAssertions.assertThat(messagesPage.getAssignedState()).contains("QA Automation Account 22");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"15525","15526"})
    @Description("Verify that user can filter discussions by mentioned user")
    public void testFilterMessagesByMentionedUser() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(currentUser);
        partsAndAssembliesDetailsPage = leftHandNavigationBar.uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickPartsAndAssemblies()
                .sortDownCreatedAtField()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName)
                .clickOnComponentName(componentName)
                .clickDigitalFactoryMessageIcon()
                .addComment("This is a discussion with a mention user @22")
                .selectMentionUser("qa-automation-22@apriori.com")
                .clickComment()
                .selectCreatedDiscussion();

        messagesPage = leftHandNavigationBar.clickMessages()
                .clickOnUnread()
                .clickOnFilter()
                .clickOnAddCondition()
                .selectMentionedUserToFilter("QA Automation Account 22")
                .clickOnFilteredDiscussion();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(messagesPage.isMentionedUserTagDisplayed("QA Automation Account 22")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"15534","15535"})
    @Description("Verify that user can filter discussions by state")
    public void testFilterMessagesByResolvedState() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(currentUser);
        partsAndAssembliesDetailsPage = leftHandNavigationBar.uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
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
                .clickOnResolveIcon();

        messagesPage = leftHandNavigationBar.clickMessages()
                .clickOnUnread()
                .clickOnFilter()
                .clickOnAddCondition()
                .selectStatusToFilter("Resolved")
                .clickOnFilteredDiscussion();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(messagesPage.getResolveStatus()).contains("resolved");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"15523","15524","16251","16252"})
    @Description("Verify that user can assign a discussion on message page")
    public void testAssignDiscussionInMessagePage() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(currentUser);
        partsAndAssembliesDetailsPage = leftHandNavigationBar.uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
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
                .shareScenario()
                .selectAUser("qa-automation-22@apriori.com")
                .selectAUser("qa-automation-23@apriori.com")
                .selectAUser("qa-automation-24@apriori.com")
                .clickOnInvite()
                .clickOnCreatedDiscussion()
                .clickMoreOption()
                .clickAssignToOption()
                .selectAParticipant("QA Automation Account 22");

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAssignedState()).contains("QA Automation Account 22");

        messagesPage = leftHandNavigationBar.clickMessages()
                .clickOnUnread();

        softAssertions.assertThat(messagesPage.isMoreOptionMenuDisplayed()).isEqualTo(true);

        messagesPage.clickOnMoreOptionMenu()
                .clickOnUnAssignToOption();

        softAssertions.assertThat(messagesPage.getDiscussionAssignedState()).doesNotContain("QA Automation Account 22");

        messagesPage.clickOnMoreOptionMenu()
                .clickOnAssignToOption();

        softAssertions.assertThat(messagesPage.isAssignToUserListDisplayed()).isEqualTo(true);

        messagesPage.selectAUserToAssign("QA Automation Account 23");

        softAssertions.assertThat(messagesPage.getDiscussionAssignedState()).contains("QA Automation Account 23");

        softAssertions.assertAll();
    }
}
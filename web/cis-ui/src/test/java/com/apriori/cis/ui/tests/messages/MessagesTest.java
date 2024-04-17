package com.apriori.cis.ui.tests.messages;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_STATE_EQ;
import static com.apriori.shared.util.enums.ScenarioStateEnum.COST_COMPLETE;

import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cis.ui.navtoolbars.LeftHandNavigationBar;
import com.apriori.cis.ui.pageobjects.login.CisLoginPage;
import com.apriori.cis.ui.pageobjects.messages.MessagesPage;
import com.apriori.cis.ui.pageobjects.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.cis.ui.utils.CisScenarioResultsEnum;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessagesTest extends TestBaseUI {

    private LeftHandNavigationBar leftHandNavigationBar;
    private MessagesPage messagesPage;
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions;
    private ScenariosUtil scenarioUtil;
    private ScenarioItem scenarioItem;
    private ComponentInfoBuilder componentInfoBuilder;
    private String userComment;
    private static final String componentName = "ChampferOut";


    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        scenarioUtil = new ScenariosUtil();
        userComment = new GenerateStringUtil().getRandomString();
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        leftHandNavigationBar = new CisLoginPage(driver).cisLogin(currentUser);
        partsAndAssembliesDetailsPage = leftHandNavigationBar.uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName());
    }

    @Test
    @TestRail(id = {13317, 13318, 13319, 13554, 13561})
    @Description("Verify messages page navigation and view all messages on the page")
    public void testMessagePageContent() {
        softAssertions.assertThat(leftHandNavigationBar.isMessagesLinkDisplayed()).isEqualTo(true);
        partsAndAssembliesDetailsPage.clickDigitalFactoryMessageIcon()
            .addComment(userComment)
            .clickComment()
            .selectCreatedDiscussion()
            .addComment("New Reply" + userComment)
            .clickComment()
            .selectCreatedDiscussion();

        messagesPage = leftHandNavigationBar.clickMessages().selectMessage(userComment);

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

        messagesPage.clickOnUnReadButton();
    }

    @Test
    @TestRail(id = {14741, 14748, 14750, 14758, 14759, 16107, 16108})
    @Description("Open a discussion from subject/attribute/replies")
    public void testMessagePageDiscussions() {
        partsAndAssembliesDetailsPage.clickDigitalFactoryMessageIcon()
            .addComment(userComment)
            .clickComment()
            .selectCreatedDiscussion()
            .addComment("New Reply" + userComment)
            .clickComment()
            .selectCreatedDiscussion();

        messagesPage = leftHandNavigationBar.clickMessages();

        partsAndAssembliesDetailsPage = messagesPage.clickOnSubjectOrAttribute("Subject");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isTrue();

        messagesPage = leftHandNavigationBar.clickMessages();

        partsAndAssembliesDetailsPage = messagesPage.clickOnReplies();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isTrue();

        messagesPage = leftHandNavigationBar.clickMessages();

        partsAndAssembliesDetailsPage = messagesPage.clickOnSubjectOrAttribute("Attribute");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isTrue();

        messagesPage = leftHandNavigationBar.clickMessages();

        softAssertions.assertThat(messagesPage.isFilterOptionDisplayed()).isTrue();

        messagesPage.clickOnFilter();

        softAssertions.assertThat(messagesPage.isFilterModalDisplayed()).isTrue();
        softAssertions.assertThat(messagesPage.isAddConditionOptionDisplayed()).isTrue();

        messagesPage.clickOnAddCondition();

        softAssertions.assertThat(messagesPage.isFilterFiledDisplayed()).isTrue();
        softAssertions.assertThat(messagesPage.isFilterConditionTypeDisplayed()).isTrue();
        softAssertions.assertThat(messagesPage.isFilterValueDisplayed()).isTrue();

        messagesPage.clickOnRemoveFilter().clickOnRead();
    }

    @Test
    @TestRail(id = {14755, 14757})
    @Description("Verify that user can filter discussions by assigned user")
    public void testFilterMessagesByAssignedUser() {
        String emailAccountName = String.format("QA Automation Account %s", currentUser.getEmail().split("-")[2].split("@")[0]);
        partsAndAssembliesDetailsPage.clickDigitalFactoryMessageIcon()
            .addComment(userComment)
            .clickComment()
            .selectCreatedDiscussion()
            .shareScenario()
            .selectAUser(currentUser.getEmail())
            .clickOnInvite()
            .clickOnCreatedDiscussion()
            .clickMoreOption()
            .clickAssignToOption()
            .selectAParticipant(emailAccountName);

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAssignedState()).contains(emailAccountName);

        messagesPage = leftHandNavigationBar.clickMessages()
            .clickOnFilter()
            .clickOnAddCondition()
            .selectAssigneeToFilter(currentUser.getEmail())
            .clickOnHeaderTitle()
            .selectMessage(userComment);

        softAssertions.assertThat(messagesPage.getAssignedState().contains(emailAccountName)).isTrue();

        messagesPage.clickOnFilteredDiscussion()
            .clickOnActiveFilter()
            .clickOnRemoveFilter()
            .clickOnHeaderTitle();
    }

    @Test
    @TestRail(id = {15525, 15526})
    @Description("Verify that user can filter discussions by mentioned user")
    public void testFilterMessagesByMentionedUser() {
        String emailAccountName = String.format("QA Automation Account %s", currentUser.getEmail().split("-")[2].split("@")[0]);
        partsAndAssembliesDetailsPage.clickDigitalFactoryMessageIcon()
            .addComment("This is a discussion " + userComment + " with a mention user @" + currentUser.getEmail().split("@")[0])
            .selectMentionUser(emailAccountName)
            .clickComment();

        messagesPage = leftHandNavigationBar.clickMessages()
            .clickOnFilter()
            .clickOnAddCondition()
            .selectMentionedUserToFilter(emailAccountName)
            .clickOnHeaderTitle()
            .selectMessage(userComment);

        softAssertions.assertThat(messagesPage.isMentionedUserTagDisplayed(emailAccountName)).isTrue();

        messagesPage.clickAddedFilter()
            .clickOnRemoveFilter()
            .clickOnHeaderTitle();
    }

    @Test
    @TestRail(id = {15534, 15535})
    @Description("Verify that user can filter discussions by state")
    public void testFilterMessagesByResolvedState() {
        partsAndAssembliesDetailsPage.clickDigitalFactoryMessageIcon()
            .addComment(userComment)
            .clickComment()
            .selectCreatedDiscussion()
            .clickOnResolveIcon();

        messagesPage = leftHandNavigationBar.clickMessages()
            .clickOnFilter()
            .clickOnAddCondition()
            .selectStatusToFilter("Resolved")
            .clickOnHeaderTitle()
            .selectMessage(userComment);

        softAssertions.assertThat(messagesPage.getResolveStatus()).contains("resolved");

        messagesPage.clickAddedFilter()
            .clickOnRemoveFilter()
            .clickOnHeaderTitle();
    }

    @Test
    @TestRail(id = {15523, 15524, 16251, 16252})
    @Description("Verify that user can assign a discussion on message page")
    public void testAssignDiscussionInMessagePage() {
        String emailAccountName = String.format("QA Automation Account %s", currentUser.getEmail().split("-")[2].split("@")[0]);
        partsAndAssembliesDetailsPage.clickDigitalFactoryMessageIcon()
            .addComment(userComment)
            .clickComment()
            .selectCreatedDiscussion()
            .shareScenario()
            .selectAUser(currentUser.getEmail())
            .selectAUser("qa-automation-23@apriori.com")
            .selectAUser("qa-automation-24@apriori.com")
            .clickOnInvite()
            .clickOnCreatedDiscussion()
            .clickMoreOption()
            .clickAssignToOption()
            .selectAParticipant(emailAccountName);

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAssignedState()).contains(emailAccountName);

        messagesPage = leftHandNavigationBar.clickMessages().selectMessage(userComment);
        softAssertions.assertThat(messagesPage.isMoreOptionMenuDisplayed()).isEqualTo(true);

        messagesPage.clickOnMoreOptionMenu().clickOnAssignToOption();
        messagesPage.selectAUserToAssign("QA Automation Account 23");
        softAssertions.assertThat(messagesPage.getDiscussionAssignedState()).contains("assigned to");
        softAssertions.assertThat(messagesPage.getDiscussionAssignedState()).contains("QA Automation Account 23");

        messagesPage.clickOnMoreOptionMenu().clickOnUnAssignToOption();
        softAssertions.assertThat(messagesPage.getDiscussionAssignedState().contains("assigned to")).isFalse();

        messagesPage.clickOnUnReadButton();
    }

    @Test
    @TestRail(id = {22677, 22678, 22679})
    @Description("Verify that user configured message view is saved")
    public void testSaveConfiguredMessageView() {
        String emailAccountName = String.format("QA Automation Account %s", currentUser.getEmail().split("-")[2].split("@")[0]);
        partsAndAssembliesDetailsPage.clickMessageIconOnCommentSection()
            .clickOnAttribute()
            .selectAttribute(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName())
            .addComment(userComment)
            .clickComment()
            .selectCreatedDiscussion()
            .addComment("This is a new reply " + userComment + " with a mention user @" + currentUser.getEmail().split("@")[0])
            .selectMentionUser(emailAccountName)
            .clickComment();

        messagesPage = leftHandNavigationBar.clickMessages()
            .clickOnFilter()
            .clickOnAddCondition()
            .selectMentionedUserToFilter(emailAccountName)
            .selectMessage(userComment);

        partsAndAssembliesDetailsPage = messagesPage.clickOnSubjectOrAttribute("Subject");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isTrue();

        messagesPage = leftHandNavigationBar.clickMessages();

        softAssertions.assertThat(messagesPage.isAddedFilterDisplayed()).isTrue();

        messagesPage.resetToDefaultConfiguration();
    }

    @AfterEach
    public void testCleanUp() {
        softAssertions.assertAll();
        scenarioItem = new CssComponent().getWaitBaseCssComponents(componentInfoBuilder.getUser(), COMPONENT_NAME_EQ.getKey() + componentInfoBuilder.getComponentName(),
            SCENARIO_NAME_EQ.getKey() + componentInfoBuilder.getScenarioName(), SCENARIO_STATE_EQ.getKey() + COST_COMPLETE).get(0);
        new ScenariosUtil().deleteScenario(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
    }
}
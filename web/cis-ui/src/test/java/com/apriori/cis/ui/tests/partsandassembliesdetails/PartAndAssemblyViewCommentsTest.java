package com.apriori.cis.ui.tests.partsandassembliesdetails;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_STATE_EQ;
import static com.apriori.shared.util.enums.ScenarioStateEnum.COST_COMPLETE;

import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cis.ui.pageobjects.login.CisLoginPage;
import com.apriori.cis.ui.pageobjects.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.cis.ui.utils.CisScenarioResultsEnum;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PartAndAssemblyViewCommentsTest extends TestBaseUI {
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions;
    private ScenariosUtil scenarioUtil;
    private ScenarioItem scenarioItem;
    private ComponentInfoBuilder componentInfoBuilder;
    private static final String componentName = "ChampferOut";

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        scenarioUtil = new ScenariosUtil();
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        partsAndAssembliesDetailsPage = new CisLoginPage(driver).cisLogin(currentUser)
            .uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName());
    }

    @Test
    @TestRail(id = {14466, 14467, 14468, 14469, 14470, 14471, 14543, 14641, 14691})
    @Description("Verify that discussions can be created by comment section")
    public void testCreateDiscussionByCommentSection() {
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMessageIconDisplayedOnComments()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickMessageIconOnCommentSection();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentThreadModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getSubject()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCancelButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCommentButtonState()).contains("Mui-disabled");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCancelButtonState()).doesNotContain("Mui-disabled");

        partsAndAssembliesDetailsPage.clickOnAttribute();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAttributeList()).contains(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName(), CisScenarioResultsEnum.PROCESS_GROUP.getFieldName(), CisScenarioResultsEnum.MATERIAL.getFieldName(),
            CisScenarioResultsEnum.MANUFACTURING.getFieldName(), CisScenarioResultsEnum.TOTAL_CAPITAL_EXPENSES.getFieldName(),
            CisScenarioResultsEnum.PIECE_PART_COST.getFieldName(), CisScenarioResultsEnum.ANNUAL_VOLUME.getFieldName(),
            CisScenarioResultsEnum.BATCH_SIZE.getFieldName(), CisScenarioResultsEnum.FINISH_MASS.getFieldName());

        partsAndAssembliesDetailsPage.selectAttribute(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName())
            .addComment("New Comment With Attribute")
            .clickComment();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionSubject()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionAttribute()).isEqualTo(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionMessage()).contains("New Comment With Attribute");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatorAvatarDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickMessageIconOnCommentSection()
            .addComment("New Comment Without Attribute")
            .clickComment();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionSubject()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDiscussionMessage()).contains("New Comment Without Attribute");
    }

    @Test
    @TestRail(id = {14183, 14184, 14185, 14186, 14187, 14691})
    @Description("Verify that replies can be added to a selected comment thread")
    public void testReplyToACommentThread() {
        partsAndAssembliesDetailsPage.clickDigitalFactoryMessageIcon()
            .addComment("New Discussion")
            .clickComment()
            .selectCreatedDiscussion();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isReplyFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isReplyButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCancelButtonOnDiscussionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCommentButtonState()).contains("Mui-disabled");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCancelButtonState()).doesNotContain("Mui-disabled");

        partsAndAssembliesDetailsPage.addComment("New Reply").clickComment();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getReplyMessage()).contains("New Reply");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getRepliesCount()).contains("1 reply");

        partsAndAssembliesDetailsPage.clickDigitalFactoryMessageIcon()
            .addComment("Second Discussion")
            .clickComment();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAttributeDiscussionCount()).contains("2");
    }

    @Test
    @TestRail(id = {14189, 14190, 14697, 14698, 14699})
    @Description("Verify that user can resolve and unresolve a discussion")
    public void testResolveAndUnresolvedComment() {
        partsAndAssembliesDetailsPage.clickMessageIconOnCommentSection()
            .clickOnAttribute()
            .selectAttribute(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName())
            .addComment("New Comment With Attribute")
            .clickComment()
            .selectCreatedDiscussion();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isResolveOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickOnResolveIcon();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getResolveStatus()).contains("resolved");

        partsAndAssembliesDetailsPage.clickOnUnResolveIcon();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getUnResolveStatus()).contains("active");
    }

    @Test
    @TestRail(id = {14195, 14196, 14201, 14202, 14730, 14732})
    @Description("Verify that user can delete and undelete a discussion")
    public void testDeleteAndUndeleteDiscussion() {
        partsAndAssembliesDetailsPage.clickMessageIconOnCommentSection()
            .clickOnAttribute()
            .selectAttribute(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName())
            .addComment("New Comment With Attribute")
            .clickComment()
            .selectCreatedDiscussion()
            .clickMoreOption();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isDeleteDiscussionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickDeleteDiscussion();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isDeleteDiscussionConfirmationModalDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickDeleteDiscussionButton();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isUndoDeleteOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickUndoDeleteDiscussionButton();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCreatedDiscussionDisplayed()).isEqualTo(true);
    }

    @Test
    @TestRail(id = {14191, 14192, 14193, 14194, 14628, 14689, 14690})
    @Description("Verify that assign and un-assign a discussion to a project participant ")
    public void testAssignUnAssignACommentThread() {
        partsAndAssembliesDetailsPage.clickDigitalFactoryMessageIcon()
            .addComment("New Discussion")
            .clickComment()
            .selectCreatedDiscussion()
            .clickMoreOption();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAssignToOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickAssignToOption();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isParticipantsListDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectAParticipant("QA Automation Account");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAssignedState()).contains("You");

        partsAndAssembliesDetailsPage.shareScenario()
            .selectAUser("qa-automation-22@apriori.com")
            .clickOnInvite()
            .clickOnCreatedDiscussion()
            .clickMoreOption();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isUnAssignOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickUnAssignOption()
            .reassignDiscussion("QA Automation Account 22");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAssignedState()).contains("QA Automation Account 22");
    }

    @Test
    @TestRail(id = {14700, 14701, 14702, 14760, 14761})
    @Description("Verify that user can delete and undelete a comment")
    public void testDeleteAndUndeleteAComment() {
        partsAndAssembliesDetailsPage
            .clickMessageIconOnCommentSection()
            .clickOnAttribute()
            .selectAttribute(CisScenarioResultsEnum.DIGITAL_FACTORY.getFieldName())
            .addComment("New Comment With Attribute")
            .clickComment()
            .selectCreatedDiscussion()
            .addComment("New Reply")
            .clickComment();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCommentMoreOptionMenuDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickOnCommentMoreOption();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isDeleteCommentOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickOnDeleteCommentOption();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isUndoDeleteOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickUnDeleteCommentOption();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isDeletedReplyDisplayed()).isEqualTo(true);

    }

    @Test
    @TestRail(id = {14728, 14729, 14731, 14736, 14733, 14734})
    @Description("Verify that mention users in a comment and assign comment to a mention user ")
    public void testMentionUsersOnACommentThread() {
        partsAndAssembliesDetailsPage
            .clickDigitalFactoryMessageIcon()
            .addComment("New Discussion")
            .clickComment()
            .selectCreatedDiscussion()
            .addCommentWithMention("This is a new reply with a mention user @22");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isMentionUserPickerDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectMentionUser("QA Automation Account");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAMentionUserTagDisplayed("QA Automation Account 22")).isEqualTo(true);

        partsAndAssembliesDetailsPage.addCommentWithMention("second mention user @23")
            .selectMentionUser("qa-automation-23@apriori.com")
            .clickChangeAssigneeOption();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAssignToAMentionUserListDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectMentionUserToAssignDiscussion("qa-automation-23@apriori.com")
            .clickToAssign()
            .clickComment();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getAssignedState()).contains("QA Automation Account 23");
    }


    @AfterEach
    public void testCleanUp() {
        softAssertions.assertAll();
        scenarioItem = new CssComponent().getWaitBaseCssComponents(componentInfoBuilder.getUser(), COMPONENT_NAME_EQ.getKey() + componentInfoBuilder.getComponentName(),
            SCENARIO_NAME_EQ.getKey() + componentInfoBuilder.getScenarioName(), SCENARIO_STATE_EQ.getKey() + COST_COMPLETE).get(0);
        new ScenariosUtil().deleteScenario(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
    }
}

package com.apriori.customer.systemconfiguration;

import static com.apriori.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.apriori.PageUtils;
import com.apriori.TestBaseUI;
import com.apriori.components.SelectionTreeItemComponent;
import com.apriori.components.SourceListComponent;
import com.apriori.components.TableComponent;
import com.apriori.components.TableHeaderComponent;
import com.apriori.http.utils.Obligation;
import com.apriori.pageobjects.customer.systemconfiguration.SystemConfigurationGroupsPage;
import com.apriori.pageobjects.login.CasLoginPage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.TimeoutException;

import java.util.List;
import java.util.stream.Collectors;

@Disabled("Feature is not fully ready yet.  Can remove this once fully built e2e")
public class SystemConfigurationGroupsTests extends TestBaseUI {
    private SystemConfigurationGroupsPage systemConfigurationGroupsPage;

    @BeforeEach
    public void setup() {

        systemConfigurationGroupsPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openAprioriInternal()
            .goToSystemConfiguration()
            .goToGroupsPage();
    }

    private SelectionTreeItemComponent validateThereIsAtLeastOneGroup() {

        List<SelectionTreeItemComponent> groups = systemConfigurationGroupsPage.getGroupsTree().getHierarchy();
        SelectionTreeItemComponent firstGroup = groups.stream().findFirst().orElse(null);
        assertThat("There are no groups to verify anything on", firstGroup, is(notNullValue()));
        return firstGroup;
    }

    private SelectionTreeItemComponent selectSomeGroupInTheMiddle() {

        List<SelectionTreeItemComponent> groups = systemConfigurationGroupsPage.getGroupsTree().getFlatHierarchy();
        int someGroupInTheMiddle = groups.size() / 2;
        SelectionTreeItemComponent groupToSelect = groups.get(someGroupInTheMiddle);
        return groupToSelect.select();
    }

    private void validateColumnHeaderIsCorrect(String expectedName, String id, TableComponent forTable, SoftAssertions soft) {

        TableHeaderComponent header = forTable.getHeader(id);
        soft.assertThat(header)
            .overridingErrorMessage("The '%s' column is missing.", expectedName)
            .isNotNull();

        if (header != null) {
            String name = header.getName();
            soft.assertThat(name)
                .overridingErrorMessage("The '%s' column is incorrectly named '%s'", expectedName, name)
                .isEqualTo(expectedName);
            soft.assertThat(header.canSort())
                .overridingErrorMessage("The '%s' column is not sortable.")
                .isTrue();
        }
    }

    private void validateFirstGroupIsSelected(SoftAssertions soft) {

        SelectionTreeItemComponent firstGroup = validateThereIsAtLeastOneGroup();
        boolean isSelected = firstGroup.isSelected();
        soft.assertThat(isSelected)
            .overridingErrorMessage("The first group is not selected.")
            .isTrue();
    }

    private void validateAllGroupsAreExpanded(SoftAssertions soft) {

        List<SelectionTreeItemComponent> all = systemConfigurationGroupsPage.getGroupsTree().getFlatHierarchy();
        List<SelectionTreeItemComponent> expandable = all.stream().filter(SelectionTreeItemComponent::isExpandable).collect(Collectors.toList());
        boolean areAllGroupsExpanded = expandable.stream().allMatch(SelectionTreeItemComponent::isExpanded);
        soft.assertThat(areAllGroupsExpanded)
            .overridingErrorMessage("There are groups that are not expanded by default.")
            .isTrue();
    }

    @Test
    @Description("Test that the groups are displayed and the first one is selected.")
    @TestRail(id = {9880, 9883, 9881, 9903})
    public void testValidateShouldDisplayGroupsForTheCustomer() {

        SoftAssertions soft = new SoftAssertions();
        validateFirstGroupIsSelected(soft);
        validateAllGroupsAreExpanded(soft);
        soft.assertAll();
    }

    private void validateRootsCollapseAndExpand(List<SelectionTreeItemComponent> groups, SoftAssertions soft) {

        for (SelectionTreeItemComponent group : groups) {
            List<SelectionTreeItemComponent> children = group.getChildren()
                .stream()
                .filter(SelectionTreeItemComponent::isExpandable)
                .collect(Collectors.toList());

            validateRootsCollapseAndExpand(children, soft);

            String groupName = group.getText();
            boolean expanded = group.collapse().isExpanded();

            soft.assertThat(expanded)
                .overridingErrorMessage("The group, %s, did not collapse properly.", groupName)
                .isFalse();

            expanded = group.expand().isExpanded();

            soft.assertThat(expanded)
                .overridingErrorMessage("The group, %s, did not expand properly.", groupName)
                .isTrue();
        }
    }

    @Test
    @Description("Test that the groups can be expanded and collapsed.")
    @TestRail(id = {9905})
    public void testValidateGroupsCanBeExpandedAndCollapsed() {

        SoftAssertions soft = new SoftAssertions();
        List<SelectionTreeItemComponent> roots = systemConfigurationGroupsPage
                .getGroupsTree()
                .getHierarchy().stream()
                .filter(SelectionTreeItemComponent::isExpandable)
                .collect(Collectors.toList());

        validateRootsCollapseAndExpand(roots, soft);
        soft.assertAll();
    }

    private void validateHeaderChangesToReflectTheSelectedGroup(SoftAssertions soft) {

        PageUtils utils = new PageUtils(driver);
        String groupName = selectSomeGroupInTheMiddle().getText();

        try {
            utils.waitForCondition(() -> {
                String detailsHeader = systemConfigurationGroupsPage.getDetailsHeader();
                String detailsGroupName = detailsHeader.replace("GROUP DETAILS -", "").trim();
                return groupName.toUpperCase().startsWith(detailsGroupName.toUpperCase());
            }, PageUtils.DURATION_SLOW);
            soft.succeeded();
        } catch (TimeoutException e) {
            String detailsHeader = systemConfigurationGroupsPage.getDetailsHeader();
            soft.fail("Expected the header for selected group, %s, to be GROUP DETAILS - %s.Actual value was %s.", groupName, groupName.toUpperCase(), detailsHeader);
        }
    }

    private void validateSelectedGroupInformation(SoftAssertions soft) {

        List<SelectionTreeItemComponent> groups = systemConfigurationGroupsPage.getGroupsTree().getFlatHierarchy();
        int someGroupInTheMiddle = groups.size() / 2;
        SelectionTreeItemComponent groupToSelect = groups.get(someGroupInTheMiddle);
        groupToSelect.select();

        // Note here that the identity is the most important and everything will just fail if the identity
        // cannot be found.
        soft.assertThat(systemConfigurationGroupsPage.getRightLabel("Identity"))
            .overridingErrorMessage("The identity label was not found or is in the wrong panel.")
            .isNotNull();

        String identity = systemConfigurationGroupsPage.getIdentity();
        assertThat("The group identity could not be found.", identity, is(notNullValue()));
        assertThat("The group identity was empty.", identity.trim().length(), is(greaterThan((0))));

        // The values need to be compared with those of the api once the api is actually ready.
        // TODO: Get the group by id from the api and compare those values once ready
        soft.assertThat(systemConfigurationGroupsPage.getLeftLabel("Number of Permissions"))
            .overridingErrorMessage("The number of permissions label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationGroupsPage.getNumberOfPermissions())
            .overridingErrorMessage("The number of permissions value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationGroupsPage.getLeftLabel("Number of Subgroups"))
            .overridingErrorMessage("The number of subgroups label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationGroupsPage.getNumberOfSubgroups())
            .overridingErrorMessage("The number of subgroups value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationGroupsPage.getLeftLabel("Created By"))
            .overridingErrorMessage("The created at label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationGroupsPage.getCreatedAt())
            .overridingErrorMessage("The created at value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationGroupsPage.getLeftLabel("Created By"))
            .overridingErrorMessage("The created by label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationGroupsPage.getCreatedBy())
            .overridingErrorMessage("The created by value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationGroupsPage.getRightLabel("Updated At"))
            .overridingErrorMessage("The updated at label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationGroupsPage.getUpdatedAt())
            .overridingErrorMessage("The updated at value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationGroupsPage.getRightLabel("Updated By"))
            .overridingErrorMessage("The updated by label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationGroupsPage.getUpdatedBy())
            .overridingErrorMessage("The updated by label was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationGroupsPage.getRightLabel("Description"))
            .overridingErrorMessage("The description label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationGroupsPage.getDescription())
            .overridingErrorMessage("The description value was not found.")
            .isNotNull();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {9906, 9942})
    public void testValidateGroupSelectionUpdatesTheSelectedDetails() {

        SoftAssertions soft = new SoftAssertions();
        validateThereIsAtLeastOneGroup();
        validateHeaderChangesToReflectTheSelectedGroup(soft);
        validateSelectedGroupInformation(soft);
        soft.assertAll();
    }

    private void validateAssociatedPermissionsArePageableAndRefreshable(SoftAssertions soft) {

        SourceListComponent list = systemConfigurationGroupsPage.getAssociatedPermissions();
        soft.assertThat(list.getPaginator())
            .overridingErrorMessage("The associated permissions table has no pagination.")
            .isNotNull();
        soft.assertThat(list.canRefresh())
            .overridingErrorMessage("The associated permissions table is missing the refresh button.")
            .isTrue();
    }

    private void validateAssociatedPermissionsHasCorrectColumns(SoftAssertions soft) {

        SourceListComponent list = systemConfigurationGroupsPage.getAssociatedPermissions();
        TableComponent table = Obligation.mandatory(list::getTable, "The associated permissions table is missing");

        validateColumnHeaderIsCorrect("Name", "name", table, soft);
        validateColumnHeaderIsCorrect("Action", "actions", table, soft);
        validateColumnHeaderIsCorrect("Resource", "resourceType", table, soft);
        validateColumnHeaderIsCorrect("Rule", "cslRule", table, soft);
        validateColumnHeaderIsCorrect("Grant", "normalGrant", table, soft);
        validateColumnHeaderIsCorrect("Deny", "normalDeny", table, soft);
        validateColumnHeaderIsCorrect("Description", "description", table, soft);
    }

    private void validateAssociatedPermissionsHasCorrectDefaultPageSize(SoftAssertions soft) {

        SourceListComponent list = systemConfigurationGroupsPage.getAssociatedPermissions();

        String pageSize = Obligation.mandatory(list::getPaginator, "The associated permissions must support pagination.")
            .getPageSize()
            .getSelected();
        soft.assertThat(pageSize)
            .overridingErrorMessage("The default selected page size for the associated permissions should be 20.")
            .isEqualTo("20");
    }

    @Test
    @TestRail(id = {9953, 9954, 9968})
    public void testValidateGroupAssociatedPermissionsHasCorrectDetails() {

        SoftAssertions soft = new SoftAssertions();
        validateThereIsAtLeastOneGroup();
        selectSomeGroupInTheMiddle();
        validateAssociatedPermissionsArePageableAndRefreshable(soft);
        validateAssociatedPermissionsHasCorrectColumns(soft);
        validateAssociatedPermissionsHasCorrectDefaultPageSize(soft);
        soft.assertAll();
    }

    private void validateMembersArePageableAndRefreshable(SoftAssertions soft) {
        SourceListComponent list = systemConfigurationGroupsPage.getMembers();
        soft.assertThat(list.getPaginator())
            .overridingErrorMessage("The members table has no pagination.")
            .isNotNull();
        soft.assertThat(list.canRefresh())
            .overridingErrorMessage("The members table is missing the refresh button.")
            .isTrue();
    }

    private void validateMembersHasCorrectColumns(SoftAssertions soft) {

        SourceListComponent list = systemConfigurationGroupsPage.getMembers();
        TableComponent table = Obligation.mandatory(list::getTable, "The members table is missing");

        validateColumnHeaderIsCorrect("Login ID", "user.username", table, soft);
        validateColumnHeaderIsCorrect("Given Name", "user.userProfile.givenName", table, soft);
        validateColumnHeaderIsCorrect("Family Name", "user.userProfile.familyName", table, soft);
        validateColumnHeaderIsCorrect("Department", "user.userProfile.department", table, soft);
    }

    private void validateMembersHasCorrectDefaultPageSize(SoftAssertions soft) {

        SourceListComponent list = systemConfigurationGroupsPage.getMembers();

        String pageSize = Obligation.mandatory(list::getPaginator, "The members table must support pagination.")
            .getPageSize()
            .getSelected();
        soft.assertThat(pageSize)
            .overridingErrorMessage("The default selected page size for the members table should be 20.")
            .isEqualTo("20");
    }

    @Test
    @TestRail(id = {9999, 10000, 10001})
    public void testValidateGroupMembersHasCorrectDetails() {

        SoftAssertions soft = new SoftAssertions();
        validateThereIsAtLeastOneGroup();
        selectSomeGroupInTheMiddle();
        validateMembersArePageableAndRefreshable(soft);
        validateMembersHasCorrectColumns(soft);
        validateMembersHasCorrectDefaultPageSize(soft);
        soft.assertAll();
    }

    private void validateAttributesAreRefreshable(SoftAssertions soft) {
        SourceListComponent list = systemConfigurationGroupsPage.getAttributes();
        soft.assertThat(list.getPaginator())
            .overridingErrorMessage("The attributes table should not be pageable.")
            .isNull();
        soft.assertThat(list.canRefresh())
            .overridingErrorMessage("The attributes table is missing the refresh button.")
            .isTrue();
    }

    private void validateAttributesHasCorrectColumns(SoftAssertions soft) {

        SourceListComponent list = systemConfigurationGroupsPage.getAttributes();
        TableComponent table = Obligation.mandatory(list::getTable, "The attributes table is missing");

        validateColumnHeaderIsCorrect("Name", "name", table, soft);
        validateColumnHeaderIsCorrect("Type", "type", table, soft);
        validateColumnHeaderIsCorrect("Value", "value", table, soft);
    }

    @Test
    @TestRail(id = {10006, 10007})
    public void testValidateGroupAttributesHasCorrectDetails() {

        SoftAssertions soft = new SoftAssertions();
        validateThereIsAtLeastOneGroup();
        selectSomeGroupInTheMiddle();
        validateAttributesAreRefreshable(soft);
        validateAttributesHasCorrectColumns(soft);
        soft.assertAll();
    }
}

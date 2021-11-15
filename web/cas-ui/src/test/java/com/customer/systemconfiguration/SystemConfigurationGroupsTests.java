package com.customer.systemconfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.apriori.customer.systemconfiguration.SystemConfigurationGroupsPage;
import com.apriori.login.CasLoginPage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.PageUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.components.SelectionTreeItemComponent;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class SystemConfigurationGroupsTests extends TestBase {
    private SystemConfigurationGroupsPage systemConfigurationGroupsPage;

    @Before
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
    @TestRail(testCaseId = {"9880", "9883", "9881", "9903"})
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
    @TestRail(testCaseId = {"9905"})
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
        List<SelectionTreeItemComponent> groups = systemConfigurationGroupsPage.getGroupsTree().getFlatHierarchy();
        int someGroupInTheMiddle = groups.size() / 2;
        SelectionTreeItemComponent groupToSelect = groups.get(someGroupInTheMiddle);
        String groupName = groupToSelect.select().getText();

        try {
            utils.waitForCondition(() -> {
                String detailsHeader = systemConfigurationGroupsPage.getGroupDetails().getHeader();
                String detailsGroupName = detailsHeader.replace("GROUP DETAILS -", "").trim();
                return groupName.toUpperCase().startsWith(detailsGroupName.toUpperCase());
            }, Duration.ofMillis(500));
            soft.succeeded();
        } catch (TimeoutException e) {
            String detailsHeader = systemConfigurationGroupsPage.getGroupDetails().getHeader();
            soft.fail("Expected the header for selected group, %s, to be GROUP DETAILS - %s.\nActual value was %s.", groupName, groupName.toUpperCase(), detailsHeader);
        }
    }

    private void validateSelectedGroupInformation(SoftAssertions soft) {

        List<SelectionTreeItemComponent> groups = systemConfigurationGroupsPage.getGroupsTree().getFlatHierarchy();
        int someGroupInTheMiddle = groups.size() / 2;
        SelectionTreeItemComponent groupToSelect = groups.get(someGroupInTheMiddle);
        groupToSelect.select();

        // Note here that the identity is the most important and everything will just fail if the identity
        // cannot be found.
        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getIdentityLabel())
            .overridingErrorMessage("The identity label was not found or is in the wrong panel.")
            .isNotNull();

        String identity = systemConfigurationGroupsPage.getGroupDetails().getIdentity();
        assertThat("The group identity could not be found.", identity, is(notNullValue()));
        assertThat("The group identity was empty.", identity.trim().length(), is(greaterThan((0))));

        // The values need to be compared with those of the api once the api is actually ready.
        // TODO: Get the group by id from the api and compare those values once ready
        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getNumberOfPermissionsLabel())
            .overridingErrorMessage("The number of permissions label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getNumberOfPermissions())
            .overridingErrorMessage("The number of permissions value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getNumberOfSubgroupsLabel())
            .overridingErrorMessage("The number of subgroups label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getNumberOfSubgroups())
            .overridingErrorMessage("The number of subgroups value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getCreatedAtLabel())
            .overridingErrorMessage("The created at label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getCreatedAt())
            .overridingErrorMessage("The created at value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getCreatedByLabel())
            .overridingErrorMessage("The created by label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getCreatedBy())
            .overridingErrorMessage("The created by value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getUpdatedAtLabel())
            .overridingErrorMessage("The updated at label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getUpdatedAt())
            .overridingErrorMessage("The updated at value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getUpdatedByLabel())
            .overridingErrorMessage("The updated by label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getUpdatedBy())
            .overridingErrorMessage("The updated by label was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getDescriptionLabel())
            .overridingErrorMessage("The description label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationGroupsPage.getGroupDetails().getDescription())
            .overridingErrorMessage("The description value was not found.")
            .isNotNull();
    }

    @Test
    @Category(SmokeTest.class)
    @TestRail(testCaseId = {"9906", "9942"})
    public void testValidateGroupSelectionUpdatesTheSelectedDetails() {

        // TODO: when a group selection changes, put the rest of the test cases in this method to verify the different data sets
        SoftAssertions soft = new SoftAssertions();
        validateThereIsAtLeastOneGroup();
        validateHeaderChangesToReflectTheSelectedGroup(soft);
        validateSelectedGroupInformation(soft);
        soft.assertAll();
    }
}

package com.customer.systemconfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
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
                String detailsHeader = systemConfigurationGroupsPage.getDetailsHeader();
                String detailsGroupName = detailsHeader.replace("GROUP DETAILS -", "").trim();
                return groupName.toUpperCase().startsWith(detailsGroupName.toUpperCase());
            }, Duration.ofMillis(500));
            soft.succeeded();
        } catch (TimeoutException e) {
            String detailsHeader = systemConfigurationGroupsPage.getDetailsHeader();
            soft.fail("Expected the header for selected group, %s, to be GROUP DETAILS - %s.\nActual value was %s.", groupName, groupName.toUpperCase(), detailsHeader);
        }
    }

    @Test
    @Category(SmokeTest.class)
    @TestRail(testCaseId = {"9906"})
    public void testValidateGroupSelectionUpdatesTheSelectedDetails() {

        // TODO: when a group selection changes, put the rest of the test cases in this method to verify the different data sets
        SoftAssertions soft = new SoftAssertions();
        validateThereIsAtLeastOneGroup();
        validateHeaderChangesToReflectTheSelectedGroup(soft);
        soft.assertAll();
    }
}

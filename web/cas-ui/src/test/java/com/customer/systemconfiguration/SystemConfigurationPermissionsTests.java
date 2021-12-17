package com.customer.systemconfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.apriori.customer.systemconfiguration.SystemConfigurationPermissionsPage;
import com.apriori.login.CasLoginPage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.ListUtils;
import com.apriori.utils.PageUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.components.SelectionTreeItemComponent;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SystemConfigurationPermissionsTests extends TestBase {
    private SystemConfigurationPermissionsPage systemConfigurationPermissionsPage;

    @Before
    public void setup() {
        systemConfigurationPermissionsPage  = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openAprioriInternal()
            .goToSystemConfiguration()
            .goToPermissionsPage();
    }

    private void testValidateThatPermissionsAreSortedInAscendingOrder(SoftAssertions soft) {
        List<SelectionTreeItemComponent> permissions = systemConfigurationPermissionsPage
            .getPermissionsList()
            .getHierarchy();
        List<String> order = permissions.stream().map(SelectionTreeItemComponent::getText).collect(Collectors.toList());
        soft.assertThat(ListUtils.isSorted(order, Comparator.nullsFirst(Comparator.naturalOrder())))
            .overridingErrorMessage("The list of permissions is not sorted.")
            .isTrue();
    }

    private void testValidateFirstPermissionIsSelected(SoftAssertions soft) {
        SelectionTreeItemComponent firstNode = systemConfigurationPermissionsPage
            .getPermissionsList()
            .getHierarchy()
            .get(0);
        soft.assertThat(firstNode.isSelected())
            .overridingErrorMessage("The first permission is not selected.")
            .isTrue();
    }

    private void testValidateNumberOfPermissionsDisplayed(SoftAssertions soft) {
        Integer numberOfPermissions = systemConfigurationPermissionsPage
            .getPermissionsList()
            .getHierarchy()
            .size();
        Integer labelCount = systemConfigurationPermissionsPage.getPermissionLabelCount();
        soft.assertThat(labelCount)
            .overridingErrorMessage("Expected the displayed number of permissions to be %s.\nActual" +
                    " value was %s.", numberOfPermissions, labelCount)
            .isEqualTo(numberOfPermissions);
    }

    @Test
    @Category(SmokeTest.class)
    @Description("System Configuration Permissions should be displayed and sorted in alphabetical order, the number " +
        "of permissions should be displayed.  The first permission should be selected and details shown correctly.")
    @TestRail(testCaseId = {"9880", "9883", "9882", "9967", "9965", "9987"})
    public void testValidateShouldDisplayPermissions() {

        SoftAssertions soft = new SoftAssertions();
        testValidateThatPermissionsAreSortedInAscendingOrder(soft);
        testValidateFirstPermissionIsSelected(soft);
        validateSelectedPermissionInformation(soft);
        testValidateNumberOfPermissionsDisplayed(soft);
        soft.assertAll();
    }

    private SelectionTreeItemComponent validateThereIsAtLeastOnePermission() {

        List<SelectionTreeItemComponent> permissions =
            systemConfigurationPermissionsPage.getPermissionsList().getHierarchy();
        SelectionTreeItemComponent firstPermission = permissions.stream().findFirst().orElse(null);
        assertThat("There are no permissions to verify anything on", firstPermission, is(notNullValue()));
        return firstPermission;
    }

    private void validateHeaderChangesToReflectTheSelectedPermission(SoftAssertions soft) {

        PageUtils utils = new PageUtils(driver);
        List<SelectionTreeItemComponent> permissions =
            systemConfigurationPermissionsPage.getPermissionsList().getFlatHierarchy();
        int somePermissionInTheMiddle = permissions.size() / 2;
        SelectionTreeItemComponent permissionsToSelect = permissions.get(somePermissionInTheMiddle);
        String permissionName = permissionsToSelect.select().getText();

        String actual = utils.findElementByText("div", permissionName,
            systemConfigurationPermissionsPage.getDetailsHeader()).getText();

        soft.assertThat(actual.toUpperCase())
            .overridingErrorMessage("Expected the header for selected permission, %s, to be %s.\nActual value was %s.", permissionName,
                permissionName.toUpperCase(), actual)
            .isEqualTo(permissionName.toUpperCase());
    }

    private void validateSelectedPermissionInformation(SoftAssertions soft) {

        List<SelectionTreeItemComponent> permissions =
            systemConfigurationPermissionsPage.getPermissionsList().getFlatHierarchy();
        int somePermissionInTheMiddle = permissions.size() / 2;
        SelectionTreeItemComponent permissionsToSelect = permissions.get(somePermissionInTheMiddle);
        permissionsToSelect.select();

        // The values need to be compared with those of the api once the api is actually ready.
        // TODO: Get the permission by id from the api and compare those values once ready
        soft.assertThat(systemConfigurationPermissionsPage.getLabel("Resource: "))
            .overridingErrorMessage("The resource label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationPermissionsPage.getValue("resource"))
            .overridingErrorMessage("The resource value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationPermissionsPage.getLabel("Description: "))
            .overridingErrorMessage("The description label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationPermissionsPage.getValue("description"))
            .overridingErrorMessage("The description value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationPermissionsPage.getLabel("Action: "))
            .overridingErrorMessage("The action label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationPermissionsPage.getValue("actions"))
            .overridingErrorMessage("The action value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationPermissionsPage.getLabel("Grant: "))
            .overridingErrorMessage("The grant label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationPermissionsPage.getValue("grant"))
            .overridingErrorMessage("The grant value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationPermissionsPage.getLabel("Deny: "))
            .overridingErrorMessage("The deny label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationPermissionsPage.getValue("deny"))
            .overridingErrorMessage("The deny value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationPermissionsPage.getLabel("CSL Rule: "))
            .overridingErrorMessage("The CSL Rule label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationPermissionsPage.getCSLRule())
            .overridingErrorMessage("The CSL Rule value was not found.")
            .isNotNull();

        soft.assertThat(systemConfigurationPermissionsPage.getLabel("JavaScript:"))
            .overridingErrorMessage("The JavaScript Rule label was not found.")
            .isNotNull();
        soft.assertThat(systemConfigurationPermissionsPage.getJSRule())
            .overridingErrorMessage("The JavaScript Rule value was not found.")
            .isNotNull();
    }

    @Test
    @Category(SmokeTest.class)
    @Description("The details page should reflect when a permission is selected.")
    @TestRail(testCaseId = {"9966"})
    public void testValidatePermissionSelectionUpdatesTheSelectedDetails() {

        SoftAssertions soft = new SoftAssertions();
        validateThereIsAtLeastOnePermission();
        validateHeaderChangesToReflectTheSelectedPermission(soft);
        soft.assertAll();
    }
}

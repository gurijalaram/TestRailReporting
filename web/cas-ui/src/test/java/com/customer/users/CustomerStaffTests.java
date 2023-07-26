package com.customer.users;

import com.apriori.GenerateStringUtil;
import com.apriori.PageUtils;
import com.apriori.TestBaseUI;
import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.entity.response.Customer;
import com.apriori.cds.entity.response.LicenseResponse;
import com.apriori.cds.entity.response.Site;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.components.CardsViewComponent;
import com.apriori.components.PaginatorComponent;
import com.apriori.components.SourceListComponent;
import com.apriori.components.TableComponent;
import com.apriori.components.TableRowComponent;
import com.apriori.customer.users.UsersListPage;
import com.apriori.customer.users.profile.UserProfilePage;
import com.apriori.http.utils.Obligation;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.login.CasLoginPage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.UserCreation;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.NoSuchElementException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CustomerStaffTests extends TestBaseUI {

    private UsersListPage usersListPage;
    private Customer targetCustomer;
    private List<User> sourceUsers;
    private CdsTestUtil cdsTestUtil;
    private String customerName;
    private String customerIdentity;
    private UserCreation userCreation;
    private IdentityHolder deleteIdentityHolder;
    private SoftAssertions soft = new SoftAssertions();

    @Before
    public void setup() {
        customerName = new GenerateStringUtil().generateCustomerName();
        String cloudRef = new GenerateStringUtil().generateCloudReference();
        String email = customerName.toLowerCase();

        cdsTestUtil = new CdsTestUtil();
        targetCustomer = cdsTestUtil.addCASCustomer(customerName, cloudRef, email).getResponseEntity();

        customerIdentity = targetCustomer.getIdentity();
        userCreation = new UserCreation();
        sourceUsers = userCreation.populateStaffTestUsers(11, customerIdentity, email);

        usersListPage = new CasLoginPage(driver)
                .login(UserUtil.getUser())
                .openCustomer(customerIdentity)
                .goToUsersPage()
                .goToCustomerStaff();
    }

    @After
    public void teardown() {
        if (deleteIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.SUBLICENSE_ASSOCIATIONS_USER_BY_ID,
                deleteIdentityHolder.customerIdentity(),
                deleteIdentityHolder.siteIdentity(),
                deleteIdentityHolder.licenseIdentity(),
                deleteIdentityHolder.subLicenseIdentity(),
                deleteIdentityHolder.userIdentity()
            );
        }

        sourceUsers.forEach(user -> cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, user.getIdentity()));
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, targetCustomer.getIdentity());
    }

    @Test
    @Description("Validate customer staff table has correct details")
    @Category(SmokeTest.class)
    @TestRail(id = {4061, 4380, 10572, 10574, 10580})
    public void testCustomerStaffTableViewHasCorrectDetails() {
        UsersListPage goToTableView = usersListPage
                .clickTableViewButton()
                .validateCustomerStaffTableArePageableRefreshable(soft)
                .validateUsersTableHasCorrectColumns("User Name", "username", soft)
                .validateUsersTableHasCorrectColumns("Identity", "identity", soft)
                .validateUsersTableHasCorrectColumns("Email", "email", soft)
                .validateUsersTableHasCorrectColumns("User Type", "userType", soft)
                .validateUsersTableHasCorrectColumns("Family Name", "userProfile.familyName", soft)
                .validateUsersTableHasCorrectColumns("Given Name", "userProfile.givenName", soft)
                .validateUsersTableHasCorrectColumns("Job Title", "userProfile.jobTitle", soft)
                .validateUsersTableHasCorrectColumns("Department", "userProfile.department", soft)
                .validateUsersTableHasCorrectColumns("Created", "createdAt", soft);

        PageUtils utils = new PageUtils(driver);

        SourceListComponent users = goToTableView.getUsersList();
        PaginatorComponent paginator = Obligation.mandatory(users::getPaginator, "The customer staff table is missing pagination.");
        paginator.getPageSize().select("10");
        paginator.clickNextPage();
        TableComponent usersTable = Obligation.mandatory(users::getTable, "The users list table is missing");

        long rows = usersTable.getRows().count();
        soft.assertThat(rows)
            .overridingErrorMessage("There are no users on next page.")
            .isGreaterThan(0L);

        paginator.clickFirstPage().getPageSize().select("20");
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);
        paginator.getPageSize().select("50");
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        String userName = sourceUsers.get(0).getUsername();
        String userIdentity = sourceUsers.get(0).getIdentity();

        goToTableView.selectUser(customerIdentity, userIdentity, userName)
                .edit()
                .cancel()
                .backToUsersListPage(UsersListPage.class)
                .clickNew()
                .backToUsersListPage();

        Obligation.mandatory(users::getSearch, "Users list search is missing").search(userName);

        SourceListComponent searchResult = goToTableView.getUsersList();
        TableComponent userFound = Obligation.mandatory(searchResult::getTable, "The user was not found");
        long count = userFound.getRows().count();
        soft.assertThat(count)
            .overridingErrorMessage("Expected 1 user is displayed")
            .isEqualTo(1L);
        soft.assertAll();
    }

    @Test
    @Description("Validate Card button switches to card view of customer staff")
    @Category(SmokeTest.class)
    @TestRail(id = {10573, 10575, 10576, 10577})
    public void testCustomerStaffCardView() {
        UsersListPage goToCardView = usersListPage
                .clickCardViewButton();

        PageUtils utils = new PageUtils(driver);

        SourceListComponent users = goToCardView.getUserListCardView();
        PaginatorComponent paginator = Obligation.mandatory(users::getPaginator, "The customer staff page is missing pagination.");
        paginator.getPageSize().select("10");

        CardsViewComponent usersGrid = Obligation.mandatory(users::getCardGrid, "The customer staff grid is missing");

        long cards = usersGrid.getCards("user-card").count();
        soft.assertThat(cards)
            .overridingErrorMessage("Expected 10 cards are displayed")
            .isEqualTo(10L);
        utils.waitForCondition(usersGrid::isStable, PageUtils.DURATION_LOADING);

        String userName = sourceUsers.get(0).getUsername();
        String userIdentity = sourceUsers.get(0).getIdentity();

        soft.assertThat(goToCardView.getFieldName(customerIdentity, userIdentity))
            .overridingErrorMessage("Expected field names are Identity, Email and Created")
            .containsExactly("Identity:", "Email:", "Created:");
        soft.assertThat(goToCardView.isIconColour(customerIdentity, userIdentity, "green"))
            .overridingErrorMessage("Icon color should be green")
            .isTrue();

        UserProfilePage openProfile = goToCardView.selectCard(customerIdentity, userIdentity);
        soft.assertThat(openProfile)
            .isNotNull();

        openProfile.backToUsersListPage(UsersListPage.class)
            .clickCardViewButton();

        Obligation.mandatory(users::getSearch, "Users list search is missing").search(userName);

        SourceListComponent searchResult = goToCardView.getUserListCardView();
        CardsViewComponent cardFound = Obligation.mandatory(searchResult::getCardGrid, "The user was not found");
        long count = cardFound.getCards("user-card").count();

        soft.assertThat(count)
            .overridingErrorMessage("Expected 1 card is displayed")
            .isEqualTo(1L);
        soft.assertAll();
    }

    @Test
    @Description("Validate license details panel")
    @TestRail(id = {13101, 13102, 13103, 13104})
    public void licenseDetailsTest() {
        String siteName = new GenerateStringUtil().generateSiteName();
        String siteId = new GenerateStringUtil().generateSiteID();
        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getIdentity();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> license = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        String licenseIdentity = license.getResponseEntity().getIdentity();
        String subLicenseIdentity = license.getResponseEntity().getSubLicenses().stream()
            .filter(x -> !x.getName().contains("master"))
            .collect(Collectors.toList()).get(0).getIdentity();
        String sublicenseName = license.getResponseEntity().getSubLicenses().stream()
            .filter(x -> !x.getName().contains("master"))
            .collect(Collectors.toList()).get(0).getName();
        String userName = sourceUsers.get(0).getUsername();
        String userIdentity = sourceUsers.get(0).getIdentity();

        cdsTestUtil.addSubLicenseAssociationUser(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);

        UsersListPage openLicenseDetails = usersListPage.clickLicenceDetailsButton("left");

        soft.assertThat(openLicenseDetails.getDetailsText())
            .overridingErrorMessage("Expected 'Select a User' placeholder is displayed")
            .isEqualTo("Select a single User");

        PageUtils utils = new PageUtils(driver);
        SourceListComponent users = usersListPage.getUsersList();
        Obligation.mandatory(users::getSearch, "Users list search is missing").search(userName);
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);
        users.selectTableLayout();
        Obligation.mandatory(users::getTable, "The table layout is not active")
            .getRows()
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("User %s is missing.", userName)))
            .getCell("identity")
            .click();

        SourceListComponent licenses = openLicenseDetails.getLicenseDetailsList();
        TableComponent licenseTable = Obligation.mandatory(licenses::getTable, "The license table is missing");

        long siteRow = licenseTable.getRows().filter(row -> row.getCell("siteName").hasValue(siteName)).count();
        soft.assertThat(siteRow)
            .overridingErrorMessage(String.format("Expected site with name %s is displayed", siteName))
            .isEqualTo(1L);

        long assignedLicense = licenseTable.getRows().filter(row -> row.getCell("subLicenseName").hasValue(sublicenseName)).count();
        soft.assertThat(assignedLicense)
            .overridingErrorMessage(String.format("Expected sublicense %s is displayed", sublicenseName))
            .isEqualTo(1L);

        openLicenseDetails.clickLicenceDetailsButton("right");
        soft.assertThat(usersListPage.isDetailsPanelOpened("right"))
            .overridingErrorMessage("Detail panel expected to be closed")
            .isFalse();
        soft.assertAll();

        deleteIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licenseIdentity)
            .subLicenseIdentity(subLicenseIdentity)
            .userIdentity(userIdentity)
            .build();
    }

    private long checkEveryOtherItem(List<TableRowComponent> rows, long pageSize) {
        long count = 0;
        for (int i = 0; i < pageSize; i += 2, ++count) {
            final TableRowComponent row = rows.get(i);
            Obligation.mandatory(row::getCheck, "The check cell is missing").check(true);
        }
        return count;
    }

    @Test
    @Description("Validate delete users can be canceled")
    @TestRail(id = {14228, 14230, 14231, 14232, 14233})
    public void deleteUserCancel() {
        soft.assertThat(usersListPage.isDeleteButtonEnable())
            .overridingErrorMessage("Delete user button expected to be disabled")
            .isFalse();

        PageUtils utils = new PageUtils(driver);
        long pageSize = 10;
        long selected = 0;
        SourceListComponent users = usersListPage.getUsersList();
        TableComponent usersTable = Obligation.mandatory(users::getTable, "The users table is missing from customer staff page");
        PaginatorComponent paginator = Obligation.mandatory(users::getPaginator, "The users table is missing pagination.");
        paginator.getPageSize().select(String.format("%d", pageSize));

        selected += checkEveryOtherItem(usersTable.getRows().collect(Collectors.toList()), pageSize);
        paginator.clickNextPage();
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);
        usersTable.getRows().findFirst().ifPresent((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").check(true));
        ++selected;

        long expected = usersTable.getRows().filter((row) -> Obligation.mandatory(row::getCheck, "The check cell is missing").isChecked()).count();
        soft.assertThat(expected)
            .overridingErrorMessage("The selection is not holding across pages.")
            .isEqualTo(selected);

        soft.assertThat(usersListPage.isDeleteButtonEnable())
            .overridingErrorMessage("Delete user button expected to be enabled")
            .isTrue();

        usersListPage.clickDeleteButton()
            .clickConfirmDeleteCancelButton();

        soft.assertThat(expected)
            .overridingErrorMessage("Expected users were not deleted")
            .isEqualTo(selected);
    }

    @Test
    @Description("Validate users can be deleted from customer staff table")
    @TestRail(id = {14229, 14234, 14235})
    public void deleteUserFromStaffTable() {
        String userName = sourceUsers.get(5).getUsername();
        PageUtils utils = new PageUtils(driver);
        SourceListComponent users = usersListPage.getUsersList();
        Obligation.mandatory(users::getSearch, "Users list search is missing").search(userName);
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        TableComponent usersTable = Obligation.mandatory(users::getTable, "The users table is missing from customer staff page");
        usersTable.getRows()
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("User %s is missing.", userName)))
            .getCheck()
            .check(true);

        usersListPage.clickDeleteButton()
            .clickConfirmDeleteOkButton();
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);
        Obligation.mandatory(users::getSearch, "Users list search is missing").search(userName);
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        sourceUsers.removeIf(user -> user.getUsername().equals(userName));

        long notDeletedUsers = usersTable.getRows().count();
        soft.assertThat(notDeletedUsers)
            .overridingErrorMessage("The staff users were not removed.")
            .isEqualTo(0L);

        usersListPage.clickOnTrashcanIcon();

        Obligation.mandatory(users::getSearch, "Users list search is missing").search(userName);
        utils.waitForCondition(users::isStable, PageUtils.DURATION_LOADING);

        long deletedUsers = usersTable.getRows().count();
        soft.assertThat(deletedUsers)
            .overridingErrorMessage("Expected soft deleted users are displayed")
            .isEqualTo(1L);
    }
}
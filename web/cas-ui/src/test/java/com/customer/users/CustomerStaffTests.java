package com.customer.users;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Customers;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.customer.users.UsersListPage;
import com.apriori.login.CasLoginPage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.Obligation;
import com.apriori.utils.PageUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.UserCreation;
import com.apriori.utils.reader.file.user.UserUtil;

import com.apriori.utils.web.components.CardsViewComponent;
import com.apriori.utils.web.components.PaginatorComponent;
import com.apriori.utils.web.components.SourceListComponent;
import com.apriori.utils.web.components.TableComponent;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CustomerStaffTests extends TestBase {

    private static final String STAFF_TEST_CUSTOMER = "StaffTestCustomer";

    private UsersListPage usersListPage;
    private Customer targetCustomer;
    private List<User> sourceUsers;
    private CdsTestUtil cdsTestUtil;
    private String customerIdentity;
    private String customerName;
    private UserCreation userCreation;

    @Before
    public void setup() {
        Map<String, Object> existingCustomer = Collections.singletonMap("name[EQ]", STAFF_TEST_CUSTOMER);
        String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String salesforce = StringUtils.leftPad(now, 15, "0");
        String email = "\\S+@".concat(STAFF_TEST_CUSTOMER);

        cdsTestUtil = new CdsTestUtil();

        targetCustomer = cdsTestUtil.findFirst(CDSAPIEnum.GET_CUSTOMERS, Customers.class, existingCustomer, Collections.emptyMap());
        targetCustomer = targetCustomer == null
                ? cdsTestUtil.addCustomer(STAFF_TEST_CUSTOMER, now, salesforce, email).getResponseEntity()
                : targetCustomer;

        customerIdentity = targetCustomer.getIdentity();
        customerName = targetCustomer.getName();
        userCreation = new UserCreation();
        sourceUsers = userCreation.populateStaffTestUsers(11, customerIdentity, customerName);

        usersListPage = new CasLoginPage(driver)
                .login(UserUtil.getUser())
                .openCustomer(customerIdentity)
                .goToUsersPage()
                .goToCustomerStaff();
    }

    @After
    public void teardown() {
        sourceUsers.forEach((user) -> cdsTestUtil.delete(CDSAPIEnum.DELETE_USERS_BY_CUSTOMER_USER_IDS, customerIdentity, user.getIdentity()));
        cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, targetCustomer.getIdentity());
    }

    @Test
    @Description("Validate customer staff table has correct details")
    @Category(SmokeTest.class)
    @TestRail(testCaseId = {"4061", "4380", "10572", "10574", "10580"})
    public void testCustomerStaffTableViewHasCorrectDetails() {
        SoftAssertions soft = new SoftAssertions();
        UsersListPage goToTableView = usersListPage
                .clickTableViewButton()
                .validateCustomerStaffTableArePageableRefreshable(soft)
                .validateUsersTableHasCorrectColumns("User Name", "username", soft)
                .validateUsersTableHasCorrectColumns("Identity", "identity", soft)
                .validateUsersTableHasCorrectColumns("Email", "email", soft)
                .validateUsersTableHasCorrectColumns("User Type", "userType", soft)
                .validateUsersTableHasCorrectColumns("Family Name", "userProfile.familyName", soft)
                .validateUsersTableHasCorrectColumns("Given Name", "userProfile.givenName", soft)
                .validateUsersTableHasCorrectColumns("Status", "active", soft)
                .validateUsersTableHasCorrectColumns("Job Title", "userProfile.jobTitle", soft)
                .validateUsersTableHasCorrectColumns("Department", "userProfile.department", soft)
                .validateUsersTableHasCorrectColumns("Created", "createdAt", soft);
        soft.assertAll();

        PageUtils utils = new PageUtils(getDriver());

        SourceListComponent users = goToTableView.getUsersList();
        PaginatorComponent paginator = Obligation.mandatory(users::getPaginator, "The customer staff table is missing pagination.");
        paginator.getPageSize().select("10");
        paginator.clickNextPage();
        TableComponent usersTable = Obligation.mandatory(users::getTable, "The users list table is missing");

        long rows = usersTable.getRows().count();
        assertThat("There are no users on next page.", rows, is(greaterThan(0L)));

        paginator.clickFirstPage().getPageSize().select("20").select("50");
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
        assertThat(count, is(equalTo(1L)));
    }

    @Test
    @Description("Validate Card button switches to card view of customer staff")
    @Category(SmokeTest.class)
    @TestRail(testCaseId = {"10573", "10575", "4371", "10576", "10577"})
    public void testCustomerStaffCardView() {
        UsersListPage goToCardView = usersListPage
                .clickCardViewButton();

        PageUtils utils = new PageUtils(getDriver());

        SourceListComponent users = goToCardView.getUserListCardView();
        PaginatorComponent paginator = Obligation.mandatory(users::getPaginator, "The customer staff page is missing pagination.");
        paginator.getPageSize().select("10");

        CardsViewComponent usersGrid = Obligation.mandatory(users::getCardGrid, "The customer staff grid is missing");

        long cards = usersGrid.getCards().count();
        assertThat(cards, is(equalTo(10L)));
        utils.waitForCondition(usersGrid::isStable, PageUtils.DURATION_LOADING);

        String userName = sourceUsers.get(0).getUsername();
        String userIdentity = sourceUsers.get(0).getIdentity();

        assertThat(goToCardView.getFieldName(customerIdentity, userIdentity), containsInRelativeOrder("Identity", "Email", "Created"));
        assertThat(goToCardView.isIconColour(customerIdentity, userIdentity,"green"), is(true));

        goToCardView.selectCard(customerIdentity, userIdentity)
                .edit()
                .changeStatus()
                .save()
                .backToUsersListPage(UsersListPage.class)
                .clickCardViewButton();

        Obligation.mandatory(users::getSearch, "Users list search is missing").search(userName);

        SourceListComponent searchResult = goToCardView.getUserListCardView();
        CardsViewComponent cardFound = Obligation.mandatory(searchResult::getCardGrid, "The user was not found");
        long count = cardFound.getCards().count();

        assertThat(count, is(equalTo(1L)));
        assertThat(goToCardView.isIconColour(customerIdentity, userIdentity, "red"), is(true));
    }
}
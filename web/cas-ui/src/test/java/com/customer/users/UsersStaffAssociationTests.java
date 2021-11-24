package com.customer.users;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.utils.CdsTestUtil;

import com.apriori.customer.users.StaffPage;
import com.apriori.login.CasLoginPage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;

public class UsersStaffAssociationTests extends TestBase {
    private List<User> sourceUsers;
    private Customer targetCustomer;
    private Customer aPrioriInternal;
    private CdsTestUtil cdsTestUtil;
    private StaffPage staffPage;

    @Before
    public void setup() {
        cdsTestUtil = new CdsTestUtil();

        aPrioriInternal = cdsTestUtil.getAprioriInternal();

        targetCustomer = cdsTestUtil.addCustomer(
            "Staff Association Test Customer",
            "##APRIORISTAFF00",
            "#APRIORISTAFF00",
            "test.com").getResponseEntity();


        sourceUsers = new ArrayList<>();

        staffPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(targetCustomer.getIdentity())
            .goToUsersPage()
            .goToStaffPage();
    }

    @After
    public void teardown() {
        sourceUsers.forEach((user) -> cdsTestUtil.delete(
            CDSAPIEnum.DELETE_USERS_BY_CUSTOMER_USER_IDS,
            aPrioriInternal.getIdentity(),
            user.getIdentity()));

        cdsTestUtil.delete(
            CDSAPIEnum.DELETE_CUSTOMER_BY_ID,
            targetCustomer.getIdentity());
    }

    private void populateStaffTestUsers(int count) {
        for (int i = 0; i < count; ++i) {
            String identity = aPrioriInternal.getIdentity();
            String username = String.format("staff-test-user-%d", i);
            String domain = aPrioriInternal.getEmailRegexPatterns().get(0).replace(".com", "");
            User added = cdsTestUtil.addUser(identity, username, domain).getResponseEntity();
            sourceUsers.add(added);
        }
    }

    @Test
    @Description("Verify staff users can be added to the list.")
    @Category(SmokeTest.class)
    @TestRail(testCaseId = {"5587"})
    public void testVerifyUsersCanBeAddedToTheList() {

        populateStaffTestUsers(21);

    }
}

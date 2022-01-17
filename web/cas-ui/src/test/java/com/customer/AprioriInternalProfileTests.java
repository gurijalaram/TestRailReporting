package com.customer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.CustomerProfilePage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;

public class AprioriInternalProfileTests extends TestBase {
    private CustomerProfilePage customerProfilePage;

    @Before
    public void setup() {

        customerProfilePage = new CasLoginPage(driver)
                .login(UserUtil.getUser())
                .openAprioriInternal()
                .goToProfile();
    }

    @Test
    @Description("Validate Edit button is disabled on customer profile tab for aPriori Internal")
    @TestRail(testCaseId = "10585")
    public void testEditButtonIsDisabledOnAprioriInternalProfile() {
        assertThat(customerProfilePage.canEdit(), is(false));
    }
}
package com.apriori.customer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.TestBaseUI;
import com.apriori.pageobjects.login.CasLoginPage;
import com.apriori.pageobjects.newcustomer.CustomerProfilePage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AprioriInternalProfileTests extends TestBaseUI {
    private CustomerProfilePage customerProfilePage;

    @BeforeEach
    public void setup() {

        customerProfilePage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openAprioriInternal()
            .goToProfile();
    }

    @Test
    @Description("Validate Edit button is disabled on customer profile tab for aPriori Internal")
    @TestRail(id = 10585)
    public void testEditButtonIsDisabledOnAprioriInternalProfile() {
        assertThat(customerProfilePage.canEdit(), is(false));
    }
}
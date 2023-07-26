package com.customer;

import com.apriori.TestBaseUI;
import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.CustomerProfilePage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;

public class AprioriInternalProfileTests extends TestBaseUI {
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
    @TestRail(id = 10585")
        public void testEditButtonIsDisabledOnAprioriInternalProfile(){
        assertThat(customerProfilePage.canEdit(), is(false));
    }
}
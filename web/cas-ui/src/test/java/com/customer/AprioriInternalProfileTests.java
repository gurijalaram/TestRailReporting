package com.customer;

import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.CustomerProfilePage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
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
        SoftAssertions soft = new SoftAssertions();

        soft.assertThat(customerProfilePage.canEdit())
                .overridingErrorMessage("Expected Edit button to be disabled and no clickable")
                .isFalse();
    }
}
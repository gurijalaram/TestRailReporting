package com.customer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.customeradmin.CustomerAdminPage;
import com.apriori.login.CasLoginPage;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;

public class CustomerListTests extends TestBase {
    private CustomerAdminPage customerAdminPage;

    @Before
    public void setup() {
        customerAdminPage = new CasLoginPage(driver).login(UserUtil.getUser());
    }

    @Test
    @Description("There is a new button on the page.")
    public void testNewButtonExists() {

        boolean actual = customerAdminPage.isNewCustomerButtonPresent();
        assertThat(actual, is(equalTo(true)));
    }
}

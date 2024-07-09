package com.apriori.cas.ui.testsuites;

import com.apriori.cas.ui.tests.customer.AprioriInternalProfileTests;
import com.apriori.cas.ui.tests.customer.CustomerAccessTests;
import com.apriori.cas.ui.tests.customer.CustomersTests;
import com.apriori.cas.ui.tests.customer.EditCustomerTests;
import com.apriori.cas.ui.tests.customer.NewCustomerTests;
import com.apriori.cas.ui.tests.infrastructure.AccessControlsApplicationTests;
import com.apriori.cas.ui.tests.navigation.NavigationTests;
import com.apriori.cas.ui.tests.security.MfaEnabledTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    NewCustomerTests.class,
    EditCustomerTests.class,
    AprioriInternalProfileTests.class,
    CustomersTests.class,
    CustomerAccessTests.class,
    NavigationTests.class,
    CasGuiUsersSuite.class,
    MfaEnabledTests.class,
    AccessControlsApplicationTests.class
})
public final class CasGuiRegressionSuite {
}

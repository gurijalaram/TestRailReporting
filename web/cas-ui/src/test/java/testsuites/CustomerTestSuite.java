package testsuites;

import com.apriori.cas.ui.tests.customer.AprioriInternalProfileTests;
import com.apriori.cas.ui.tests.customer.CustomerAccessTests;
import com.apriori.cas.ui.tests.customer.CustomersTests;
import com.apriori.cas.ui.tests.customer.EditCustomerTests;
import com.apriori.cas.ui.tests.customer.NewCustomerTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    NewCustomerTests.class,
    EditCustomerTests.class,
    AprioriInternalProfileTests.class,
    CustomersTests.class,
    CustomerAccessTests.class
})
public class CustomerTestSuite {
}

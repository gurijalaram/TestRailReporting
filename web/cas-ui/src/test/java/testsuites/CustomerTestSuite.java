package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.customer.AprioriInternalProfileTests;
import com.customer.CustomerAccessTests;
import com.customer.CustomersTests;
import com.customer.EditCustomerTests;
import com.customer.NewCustomerTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("874")
@RunWith(CategorySuiteRunner.class)
@Suite.SuiteClasses({
    NewCustomerTests.class,
    EditCustomerTests.class,
    AprioriInternalProfileTests.class,
    CustomersTests.class,
    CustomerAccessTests.class
})
public class CustomerTestSuite {
}

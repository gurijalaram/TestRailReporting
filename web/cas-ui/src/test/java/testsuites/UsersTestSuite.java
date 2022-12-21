package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.customer.users.BatchImportListTests;
import com.customer.users.CustomerStaffTests;
import com.customer.users.EditUserTests;
import com.customer.users.NewUserTests;
import com.customer.users.UsersGrantApplicationAccessTests;
import com.customer.users.UsersGrantLicenseTests;
import com.customer.users.UsersStaffAssociationTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("874")
@RunWith(CategorySuiteRunner.class)
@Suite.SuiteClasses({
    UsersStaffAssociationTests.class,
    CustomerStaffTests.class,
    NewUserTests.class,
    EditUserTests.class,
    BatchImportListTests.class,
    UsersGrantApplicationAccessTests.class,
    UsersGrantLicenseTests.class
})
public class UsersTestSuite {
}

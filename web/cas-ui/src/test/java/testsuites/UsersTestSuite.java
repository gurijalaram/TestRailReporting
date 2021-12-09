package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.customer.users.UsersStaffAssociationTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("874")
@RunWith(CategorySuiteRunner.class)
@Suite.SuiteClasses({
    UsersStaffAssociationTests.class,
})
public class UsersTestSuite {
}
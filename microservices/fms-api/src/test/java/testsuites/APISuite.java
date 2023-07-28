package testsuites;

import com.apriori.FileManagementControllerTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@SelectClasses({
    FileManagementControllerTest.class
})
public class APISuite {
}

package testsuites;

import com.apriori.cnh.api.tests.CnhNegativeTests;
import com.apriori.cnh.api.tests.CnhPositiveTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CnhPositiveTests.class,
    CnhNegativeTests.class
})

public class APISuite {
}

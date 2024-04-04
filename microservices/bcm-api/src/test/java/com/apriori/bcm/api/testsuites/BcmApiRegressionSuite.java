package com.apriori.bcm.api.testsuites;

import com.apriori.bcm.api.tests.AnalysisInputsTests;
import com.apriori.bcm.api.tests.CostWorksheetTests;
import com.apriori.bcm.api.tests.InputRowTests;
import com.apriori.bcm.api.tests.UpdateWorksheetTests;
import com.apriori.bcm.api.tests.WorksheetTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    WorksheetTests.class,
    UpdateWorksheetTests.class,
    AnalysisInputsTests.class,
    CostWorksheetTests.class,
    InputRowTests.class
})
public class BcmApiRegressionSuite {
}

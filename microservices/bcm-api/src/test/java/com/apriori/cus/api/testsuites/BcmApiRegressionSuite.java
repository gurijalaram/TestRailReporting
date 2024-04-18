package com.apriori.cus.api.testsuites;

import com.apriori.cus.api.tests.AnalysisInputsTests;
import com.apriori.cus.api.tests.CostWorksheetTests;
import com.apriori.cus.api.tests.InputRowTests;
import com.apriori.cus.api.tests.UpdateWorksheetTests;
import com.apriori.cus.api.tests.WorksheetTests;

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
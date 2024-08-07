package com.apriori.trr.api.testrail;

import com.apriori.trr.api.testrail.controller.HtmlReport;
import com.apriori.trr.api.testrail.controller.ProjectTestCase;
import com.apriori.trr.api.testrail.model.Case;
import com.apriori.trr.api.testrail.model.CaseField;
import com.apriori.trr.api.testrail.model.Project;

import java.util.List;
import java.util.stream.Collectors;

public class TestRailUtil {

    TestRailConfig testRailConfig;
    TestRail testRail;

    public TestRailUtil() {
        this.testRailConfig = new TestRailConfig();
        this.testRail = TestRail.builder(testRailConfig.getBaseApiUrl(),
            testRailConfig.getUsername(), testRailConfig.getPassword()).build();
    }

    public List<Project> getProjects() {
        return testRail.projects().list().execute();
    }

    public ProjectTestCase getTestCases(Integer projectId, String projectName) {
        List<Case> testCases = testRail.cases().list(projectId).execute();

        List<Case> automatedList  =  testCases.stream().filter(aCase -> Integer.parseInt(aCase.getCustom_automation_type()) == 2).collect(Collectors.toList());
        List<Case> automatableList  =  testCases.stream().filter(aCase -> Integer.parseInt(aCase.getCustom_automation_type()) == 1).collect(Collectors.toList());
        ProjectTestCase ptc = new ProjectTestCase();
        ptc.setProjectID(4);
        ptc.setProjectName(projectName);
        ptc.setAutomated(automatedList.size());
        ptc.setAutomatable(automatableList.size());
        ptc.setTotalCases(automatedList.size() + automatableList.size());
        HtmlReport htmlReport = new HtmlReport();
        htmlReport.generateTestReport(ptc);
        return ptc;
    }

}
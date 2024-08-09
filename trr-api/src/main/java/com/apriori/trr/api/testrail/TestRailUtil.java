package com.apriori.trr.api.testrail;

import com.apriori.trr.api.testrail.controller.HtmlReport;
import com.apriori.trr.api.testrail.controller.ProjectTestCase;
import com.apriori.trr.api.testrail.controller.ProjectTestCases;
import com.apriori.trr.api.testrail.model.Case;
import com.apriori.trr.api.testrail.model.CaseField;
import com.apriori.trr.api.testrail.model.Project;
import com.apriori.trr.api.testrail.model.Suite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestRailUtil {

    private static final Logger log = LoggerFactory.getLogger(TestRailUtil.class);
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

    public List<Suite> getSuites(Integer projectId) {
        List<Suite> suites = testRail.suites().list(projectId).execute();
        return suites;
    }

    public ProjectTestCase getTestCases(Integer projectId, String projectName) {
        List<Case> testCases = testRail.cases().list(projectId).execute();
        String automationType = "";
        try {
            Integer count = 0;
            for (Case c : testCases) {
                if (c.getCustom_automation_type() == null) {
                    automationType = String.format(" AUTOMATION TYPE IS NOT ASSIED FOR Project %s == TEST CASE ID : %d \n", projectName, c.getId());
                    log.warn(automationType);
                } else {
                    Integer ct = Integer.parseInt(c.getCustom_automation_type());
                    System.out.println(ct);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        List<Case> automatedList = testCases.stream()
            .filter(aCase -> !(aCase.getCustom_automation_type() == null))
            .filter(aCase -> Integer.parseInt(aCase.getCustom_automation_type()) == 2)
            .collect(Collectors.toList());

        List<Case> automatableList = testCases.stream()
            .filter(aCase -> !(aCase.getCustom_automation_type() == null))
            .filter(aCase -> Integer.parseInt(aCase.getCustom_automation_type()) == 1)
            .collect(Collectors.toList());

        //  List<Case> automatedList  =  testCases.stream().filter(aCase -> Integer.parseInt(aCase.getCustom_automation_type()) == 2).collect(Collectors.toList());
        //  List<Case> automatableList  =  testCases.stream().filter(aCase -> Integer.parseInt(aCase.getCustom_automation_type()) == 1).collect(Collectors.toList());
        ProjectTestCase ptc = new ProjectTestCase();

        ptc.setProjectID(projectId);
        ptc.setProjectName(projectName);
        ptc.setAutomated(automatedList.size());
        ptc.setAutomatable(automatableList.size());
        ptc.setTotalCases(automatableList.size() + automatedList.size());
        return ptc;
    }

}
package com.apriori.trr.api.tests;

import com.apriori.trr.api.testrail.TestRail;
import com.apriori.trr.api.testrail.TestRailConfig;
import com.apriori.trr.api.testrail.TestRailRule;
import com.apriori.trr.api.testrail.TestRailUtil;
import com.apriori.trr.api.testrail.controller.HtmlReport;
import com.apriori.trr.api.testrail.controller.ProjectTestCase;
import com.apriori.trr.api.testrail.model.Case;
import com.apriori.trr.api.testrail.model.CaseField;
import com.apriori.trr.api.testrail.model.Project;
import com.apriori.trr.api.testrail.rules.TestRulesAPI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class AchCustomersTests {
    private TestRailUtil testRailUtil;

    @BeforeEach
    public void setup() {
    }

    @Test
    @TestRailRule(id = 1)
    public void getProjects() {
        TestRailConfig testRailConfig = new TestRailConfig();
        TestRail testRail = TestRail.builder(testRailConfig.getBaseApiUrl(),
            testRailConfig.getUsername(), testRailConfig.getPassword()).build();

        List<Project> pl = testRail.projects().list().execute();
        System.out.println(pl.get(0));
    }

    @Test
    public void testCoverageReport() {
        TestRailConfig testRailConfig = new TestRailConfig();
        TestRail testRail = TestRail.builder(testRailConfig.getBaseApiUrl(),
            testRailConfig.getUsername(), testRailConfig.getPassword()).build();

        List<Project> pl = testRail.projects().list().execute();
        System.out.println(pl.get(0));
    }

    @Test
    public void getCases() {
        TestRailConfig testRailConfig = new TestRailConfig();
        TestRail testRail = TestRail.builder(testRailConfig.getBaseApiUrl(),
            testRailConfig.getUsername(), testRailConfig.getPassword()).build();
        List<CaseField> caseFields = testRail.caseFields().list().execute();

        List<CaseField> caseFieldList = caseFields.stream().filter(caseField -> caseField.getId() == 14).collect(Collectors.toList());

        List<Case> testCases = testRail.cases().list(1, caseFieldList).execute();
        testCases.stream().forEach(System.out::println);
    }

    @Test
    public void getTestCases() {
        List<ProjectTestCase> projectTestCases = new ArrayList<>();
        HtmlReport htmlReport = new HtmlReport();
        List<Project> projectList = new TestRailUtil().getProjects().stream()
            .filter(p -> !p.getName().contains("Inactive"))
            .filter(p -> !p.getName().contains("Cloud Futures"))
            .filter(p -> !p.getName().contains("Cloud Migration"))
            .filter(p -> p.getId()!=10).collect(Collectors.toList());
        
           projectList.stream() .forEach(p -> {
            projectTestCases.add(new TestRailUtil().getTestCases(p.getId(), p.getName()));
        });

        htmlReport.generateTestReport(projectTestCases);

    }
}
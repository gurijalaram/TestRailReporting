package com.apriori.testrail;

import com.apriori.LoadProperties;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.model.Result;
import com.codepine.api.testrail.model.ResultField;
import com.codepine.api.testrail.model.Run;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author cfrith
 * For original code @see <a href="https://github.com/sandeeprao/testrail">TestRail</a>
 * Explanation @see <a href="https://medium.com/@sandeep12.rao/automating-unit-test-results-reporting-to-testrail-658d4bf97763">Code Explanation</a>
 */
public class TestRailReport {
    private static List<Result> results = new ArrayList<>();
    private static int projectId;

    public static void addResult(Result result, int projectId) {
        results.add(result);
        TestRailReport.projectId = projectId;
    }

    public static void reportResults() {
        Properties properties = LoadProperties.loadProperties("testrail");
        String url = properties.getProperty("url").trim();
        String userId = properties.getProperty("username").trim();
        String pwd = properties.getProperty("password").trim();
        TestRail testRail = TestRail.builder(url, userId, pwd).build();

        Run run = testRail.runs().get(projectId).execute();
        List<ResultField> customResultFields = testRail.resultFields().list().execute();
        testRail.results().addForCases(run.getId(), results, customResultFields).execute();
    }
}

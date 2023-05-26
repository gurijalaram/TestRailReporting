package com.compare;

import static com.utils.ColumnsEnum.COMPONENT_NAME;
import static com.utils.ColumnsEnum.COST_MATURITY;
import static com.utils.ColumnsEnum.SCENARIO_NAME;
import static com.utils.ColumnsEnum.STATUS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.compare.ModifyComparisonPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.OperationEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.PropertyEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.ComparisonCardEnum;
import com.utils.ComparisonDeltaEnum;
import com.utils.DirectionEnum;
import com.utils.EvaluateDfmIconEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ExtendedRegression;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class QuickComparisonTests  extends TestBase {

    private final String notFoundMessage = "Oops! Looks like the component or scenario you were looking for could not be found.";
    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ComparePage comparePage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private ModifyComparisonPage modifyComparisonPage;
    private File resourceFile;
    private File resourceFile2;
    private File resourceFile3;
    private File resourceFile4;
    private File resourceFile5;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private ComponentInfoBuilder cidComponentItemC;
    private ComponentInfoBuilder cidComponentItemB;
    private ComponentInfoBuilder cidComponentItem;
    private SoftAssertions softAssertions = new SoftAssertions();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();

    public QuickComparisonTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"7019"})
    @Description("User can create a comparison by multi selection two or more components on explore page")
    public void createComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "M3CapScrew";
        String componentName2 = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .createComparison()
            .selectQuickComparison()
            .create();

        softAssertions.assertThat(comparePage.getBasis()).as("Verify Comparison Basis Scenario Name")
            .isEqualTo(componentName.toUpperCase() + "  / " + scenarioName);
        //comparePage.getScenariosInComparison().forEach(scenario -> softAssertions.assertThat(scenario.equals()));

        softAssertions.assertAll();
    }
}

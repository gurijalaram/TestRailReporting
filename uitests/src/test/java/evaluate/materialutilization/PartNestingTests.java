package evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.PartNestingPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

public class PartNestingTests extends TestBase {

    private CIDLoginPage loginPage;
    private MaterialPage materialPage;
    private PartNestingPage partNestingPage;

    public PartNestingTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"906"})
    @Description("Validate Part Nesting Tab can be accessed")
    public void partNestingTabAccessible() {
        loginPage = new CIDLoginPage(driver);
        partNestingPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .openMaterialComposition()
            .goToPartNestingTab();
        assertThat(partNestingPage.getSelectedSheet(), is(equalTo("4.00 mm x 1250 mm x 2500 mm")));
        assertThat(partNestingPage.getBlankSize(), is(equalTo("470.7811 x 400.0018")));
        assertThat(partNestingPage.getPartsPerSheet(), is(equalTo("15")));
    }

    @Test
    @TestRail(testCaseId = {})
    @Description("Select Rectangular method of Part Nesting and cost")
    public void partNestingTabRectangularNesting() {
        loginPage = new CIDLoginPage(driver);
        partNestingPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .openMaterialComposition()
            .goToPartNestingTab()
            .selectRectangularNesting()
            .closePartNestingPanel()
            .costScenario()
            .openMaterialComposition()
            .goToPartNestingTab();

        assertThat(partNestingPage.isRectangularNesting("checked"), is("true"));
    }

    @Test
    @TestRail(testCaseId = {})
    @Description("Select True Part method of Part Nesting and cost")
    public void partNestingTabTruePartNesting() {
        loginPage = new CIDLoginPage(driver);
        partNestingPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .openMaterialComposition()
            .goToPartNestingTab()
            .selectRectangularNesting()
            .selectTrue_PartShapeNesting()
            .closePartNestingPanel()
            .costScenario()
            .openMaterialComposition()
            .goToPartNestingTab();

        assertThat(partNestingPage.isTruePartNesting("checked"), is("true"));
        }

        @Test
        @TestRail(testCaseId = {})
        @Description("Select Machine Default method of Part Nesting and cost")
        public void partNestingTabMachineDefaultNesting() {
        loginPage = new CIDLoginPage(driver);
        partNestingPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .openMaterialComposition()
            .goToPartNestingTab()
            .selectMachineDefaultNesting()
            .closePartNestingPanel()
            .costScenario()
            .openMaterialComposition()
            .goToPartNestingTab();

        assertThat(partNestingPage.isMachineDefaultNesting("checked"), is("true"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"907"})
    @Description("Validate Part Nesting Tab can not be accessed for inappropriate Process Groups")
    public void partNestingTabDisabled() {
        loginPage = new CIDLoginPage(driver);
        materialPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openMaterialComposition()
            .expandPanel();

        assertThat(materialPage.getPartNestingButton().getAttribute("class"), is("disabled"));
    }
}

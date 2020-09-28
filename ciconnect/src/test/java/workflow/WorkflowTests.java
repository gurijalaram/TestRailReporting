package workflow;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.web.driver.TestBase;

import enums.ConnectorsEnum;
import login.LoginPage;
import org.junit.Test;
import workflows.GenericWorkflow;
import workflows.NewEditWorkflow;
import workflows.Schedule;

public class WorkflowTests extends TestBase {

    private GenericWorkflow genericWorkflow;
    private Schedule schedule;
    private NewEditWorkflow newEditWorkflow;

    public WorkflowTests() {
        super();
    }

    @Test
    public void testSimpleEndToEnd() throws InterruptedException {
        String workflowName = new GenerateStringUtil().generateScenarioName();

        schedule = new LoginPage(driver)
            .login("qa-automation-02@apriori.com", "TrumpetSnakeFridgeToasty18!")
            .clickScheduleTab()
            .clickNewWorkflowBtn()
            .inputWorkflowName(workflowName)
            .selectConnector(ConnectorsEnum.WINDCHILL_CONNECTOR.getConnectorName())
            .clickDetailsNextBtn()
            .selectQueryCIConnectField(0, "Part Number")
            .enterQueryArgument(0, "0000000313")
            .clickQueryNextBtn()
            .clickCostingInputsNextBtn()
            .clickSaveButton()
            .selectWorkflow(workflowName)
            .clickDeleteBtn()
            .clickConfirmDeleteBtn();

        assertThat(schedule.isWorkflowInTable(workflowName), is(false));
    }
}

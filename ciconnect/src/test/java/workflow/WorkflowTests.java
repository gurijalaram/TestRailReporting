package workflow;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.web.driver.TestBase;

import enums.ConnectorsEnum;
import io.qameta.allure.Description;
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
    @TestRail(testCaseId = {"3588"})
    @Description("Test the creation and deletion of a workflow and that appropriate error message is displayed")
    public void testDeleteWorkflow() throws InterruptedException {
        String workflowName = new GenerateStringUtil().generateScenarioName();

        schedule = new LoginPage(driver)
            .login()
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
            .clickDeleteBtn();

        assertThat(schedule.getDeleteConfirmationText(), equalTo("Do you really want to delete this workflow? This action cannot be undone."));

        schedule.clickConfirmDeleteBtn();

        assertThat(schedule.isWorkflowInTable(workflowName), is(false));
    }
}

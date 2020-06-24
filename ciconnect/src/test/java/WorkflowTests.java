import com.apriori.utils.web.driver.TestBase;

import org.junit.Test;
import workflows.GenericWorkflow;
import workflows.NewEditWorkflow;
import workflows.Schedule;

public class WorkflowTests extends TestBase {

    private GenericWorkflow genericWorkflow;
    private Schedule schedule;
    private NewEditWorkflow newEditWorkflow;

    public WorkflowTests() {super();}

    @Test
    public void testSimpleEndtoEnd(){
        newEditWorkflow = new LoginPage(driver)
            .login(driver)
            .clickScheduleTab()
            .clickNewWorkflowBtn()
            .inputWorkflowName("Auto WF 1")
            .selectConnector("WC - ConnectorAgent RC3");
    }

}

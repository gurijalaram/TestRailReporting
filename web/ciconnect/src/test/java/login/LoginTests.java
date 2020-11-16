package login;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.web.driver.TestBase;

import org.junit.Test;
import workflows.GenericWorkflow;

public class LoginTests extends TestBase {

    private GenericWorkflow genericWorkflow;

    public LoginTests() {
        super();
    }

    @Test
    public void testLogin() {
        genericWorkflow = new LoginPage(driver).login();
        assertThat(genericWorkflow.getWorkflowText(), equalTo("Workflows"));
    }
}
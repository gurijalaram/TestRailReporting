package agent.tests;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.AgentConstants;
import utils.AgentService;
import utils.CicLoginUtil;

public class AgentInstallTest extends TestBase {

    private static String loginSession;
    private static SoftAssertions softAssertions;
    private static AgentService agentService;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        loginSession = new CicLoginUtil(driver).login(UserUtil.getUser()).navigateToUserMenu().getWebSession();
        agentService = new AgentService();
    }

    @Test
    @Description("1. Search Nexus Repository for cic agent and for latest version" +
        "2. Download the latest agent in zip format" +
        "3. Extract the zip file to a folder with agent version name " +
        "4. Login CI-Connect web to get the connector information" +
        "5. Check for connector existence or else create new connector" +
        "6. Get the connector options information" +
        "7. Updated extracted options file with retrieved connector options" +
        "8. Connect to agent remote server and uninstall existing agent" +
        "9. Upload the extracted agent folder to remote server and install the agent" +
        "10. Install the certificates and verify connector is connected to PLM system")
    public void downloadAndInstallAgent() {
        agentService.searchNexusRepositoryByGroup()
            .downloadAgentFile()
            .unZip()
            .getConnector(loginSession)
            .getConnectorOptions()
            .updateOptionsFile()
            .createRemoteSession()
            .getSftpConnection()
            .cleanUnInstall()
            .uploadAndInstall()
            .installCertificates()
            .close();

        softAssertions.assertThat(agentService.getConnectorStatusInfo().getConnectionStatus()).isEqualTo("Connected to PLM");
    }

    @After
    public void testCleanup() {
        softAssertions.assertAll();
    }
}

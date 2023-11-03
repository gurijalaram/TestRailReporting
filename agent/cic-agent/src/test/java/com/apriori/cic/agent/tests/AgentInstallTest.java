package com.apriori.cic.agent.tests;

import com.apriori.cic.agent.utils.AgentService;
import com.apriori.cic.api.utils.CicLoginUtil;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AgentInstallTest extends TestBaseUI {

    private static String loginSession;
    private static SoftAssertions softAssertions;
    private static AgentService agentService;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        loginSession = new CicLoginUtil(driver).login(UserUtil.getUser()).navigateToUserMenu().getWebSession();
        agentService = new AgentService();
    }

    @Test
    @TestRail(id = {4736, 4738, 4739, 4737})
    @Description("1. Search Nexus Repository for cic agent and for latest version" +
        "2. Download the latest agent in zip format" +
        "3. Extract the zip file to a folder with agent version name " +
        "4. Login CI-Connect web to get the connector information" +
        "5. Check for connector existence or else create new connector" +
        "6. Get the connector options information" +
        "7. Updated extracted options file with retrieved connector options" +
        "8. 4739 - Connect to agent remote server and uninstall existing agent" +
        "9. Upload the extracted agent folder to remote server and install the agent" +
        "10. 4738 - Start / Stop Agent Service" +
        "11. 4738,  4737 - Install the certificates and verify connector is connected to PLM system" +
        "12. This test covers installation and uninstallation of FileSystem, Windchill, and Teamcenter in CLI mode.")
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
            .executeAgentService()
            .close();

        softAssertions.assertThat(agentService.getConnectorStatusInfo().getConnectionStatus()).isEqualTo("Connected to PLM");
    }

    @AfterEach
    public void testCleanup() {
        softAssertions.assertAll();
    }
}

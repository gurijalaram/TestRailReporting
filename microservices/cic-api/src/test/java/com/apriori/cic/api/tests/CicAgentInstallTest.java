package com.apriori.cic.api.tests;

import com.apriori.cic.api.agent.Agent;
import com.apriori.cic.api.agent.AgentRepository;
import com.apriori.cic.api.agent.AgentService;
import com.apriori.cic.api.utils.CicLoginUtil;
import com.apriori.shared.util.enums.RolesEnum;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.nexus.utils.NexusComponent;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CicAgentInstallTest extends TestBaseUI {

    private static String loginSession;
    private static SoftAssertions softAssertions;
    private static Agent agent;
    private static AgentRepository agentRepository;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        loginSession = new CicLoginUtil(driver).login(UserUtil.getUser(RolesEnum.APRIORI_DEVELOPER)).navigateToUserMenu().getWebSession();
        agent = AgentService.getAgent();
        agentRepository = new AgentRepository();
    }

    @Test
    @TestRail(id = {5043, 5061, 5062, 5058, 5057})
    @Description("1. Search Nexus Repository for cic agent and for latest version" +
        "2. Download the latest agent in zip format" +
        "3. Extract the zip file to a folder with agent version name " +
        "4. Login CI-Connect web to get the connector information" +
        "5. Check for connector existence or else create new connector" +
        "6. Get the connector options information" +
        "7. Updated extracted options file with retrieved connector options" +
        "8. 4739 - Connect to agent remote server and uninstall existing agent" +
        "9. Upload the extracted agent folder to remote server and install the agent" +
        "10. 5058, 5057 - Start / Stop Agent Service" +
        "11. 5577 - Install the certificates and verify connector is connected to PLM system" +
        "12. This test covers installation and uninstallation of FileSystem, Windchill, and Teamcenter in CLI mode.")
    public void downloadAndInstallAgent() {
        NexusComponent nexusComponent = agentRepository.downloadAgent(agentRepository.searchNexusRepositoryByGroup());
        agent.unInstall()
            .setConnectorOptions(nexusComponent, loginSession)
            .install(nexusComponent)
            .closeConnection();
        softAssertions.assertThat(agent.getConnectorStatusInfo(loginSession).getConnectionStatus()).isEqualTo("Connected to PLM");
    }

    @AfterEach
    public void testCleanup() {
        softAssertions.assertAll();
    }
}

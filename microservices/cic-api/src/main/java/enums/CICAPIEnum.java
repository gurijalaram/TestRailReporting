package enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

import org.apache.commons.lang3.StringUtils;

public enum CICAPIEnum implements ExternalEndpointEnum {

    CIC_AGENT_STATUS("api/status"),
    CIC_AGENT_CONFIG("api/configuration"),
    CIC_AGENT_WORKFLOWS("api/workflows"),
    CIC_AGENT_WORKFLOW("api/workflows/%s"),
    CIC_AGENT_WORKFLOW_JOBS("api/workflows/%s/jobs"),
    CIC_AGENT_WORKFLOW_RUN("api/workflows/%s/run"),
    CIC_AGENT_WORKFLOW_JOB_CANCEL("api/workflows/%s/jobs/%s/cancel"),
    CIC_AGENT_WORKFLOW_RUN_PARTS_LIST("api/workflows/%s/runPartList"),
    CIC_AGENT_WORKFLOW_JOB_STATUS("api/workflows/%s/jobs/%s"),
    CIC_AGENT_WORKFLOW_JOB_RESULT("api/workflows/%s/jobs/%s/results"),
    CIC_AGENT_WORKFLOW_JOB_PART_RESULT("api/workflows/%s/jobs/%s/parts/%s/results"),

    CIC_UI_CREATE_WORKFLOW("Thingworx/Things/PLMC_NewSchedulePopup/Services/CreateJobDefinition"),
    CIC_UI_DELETE_WORKFLOW("Thingworx/Things/PLMC_JobManagement/Services/DeleteJobDefinition"),

    CIC_UI_CREATE_CONNECTOR("Thingworx/Things/PLMC_NewAgentPopup/Services/CreateAgent"),
    CIC_UI_GET_CONNECTORS("Thingworx/Things/PLMC_NewAgentPopup/Services/GetConnectors"),
    CIC_UI_GET_AGENT_CONNECTION_INFO("Thingworx/Things/PLMC_NewAgentPopup/Services/GetThingworxConnectionInfo"),
    CIC_UI_GET_WORKFLOW_REPORT_TEMPLATES("Thingworx/Things/PLMC_NewSchedulePopup/Services/GetReportTemplateNames"),

    CIC_PLM_WC_SEARCH("/servlet/rest/search/objects");

    private final String endpoint;

    CICAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return getAgentUrl() + String.format(getEndpointString(), variables);
    }

    public String getAgentUrl() {
        String apiUrl = StringUtils.EMPTY;
        if (this.endpoint.contains("Thingworx")) {
            apiUrl = PropertiesContext.get("ci-connect.ui_url");
        } else if (this.endpoint.contains("servlet")) {
            apiUrl = PropertiesContext.get("ci-connect.windchill.host_name");
        } else {
            apiUrl = PropertiesContext.get("ci-connect.agent_api_url") + ":" + PropertiesContext.get("${customer}.ci-connect.${${customer}.ci-connect.agent_type}.port") + "/";
        }
        return apiUrl;
    }
}

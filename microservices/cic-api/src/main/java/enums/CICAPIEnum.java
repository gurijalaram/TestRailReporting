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

    CIC_PLM_WC_SEARCH("Windchill/servlet/rest/search/objects");

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
        String cicUri = StringUtils.EMPTY;
        if (this.endpoint.contains("Thingworx")) {
            cicUri = PropertiesContext.get("${env}.ci-connect.ui_url") + String.format(getEndpointString(), variables);
        } else if (this.endpoint.contains("Windchill")) {
            cicUri = PropertiesContext.get("${env}.ci-connect.plm_wc_api_url") + String.format(getEndpointString(), variables);
        } else {
            cicUri = PropertiesContext.get("${env}.ci-connect.${customer}.agent_api_url") + String.format(getEndpointString(), variables);
        }
        return cicUri;
    }
}

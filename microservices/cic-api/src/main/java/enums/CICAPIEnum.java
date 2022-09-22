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
    CIC_AGENT_WORKFLOW_JOB("api/workflows/%s/jobs/%s"),
    CIC_AGENT_WORKFLOW_RUN("api/workflows/%s/run"),
    CIC_AGENT_WORKFLOW_JOB_CANCEL("api/workflows/%s/jobs/%s/cancel"),
    CIC_UI_CREATE_WORKFLOW("Thingworx/Things/PLMC_NewSchedulePopup/Services/CreateJobDefinition"),
    CIC_UI_DELETE_WORKFLOW("Thingworx/Things/PLMC_JobManagement/Services/DeleteJobDefinition");


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
        } else {
            cicUri = PropertiesContext.get("${env}.ci-connect.agent_api_url") + String.format(getEndpointString(), variables);
        }
        return cicUri;
    }
}
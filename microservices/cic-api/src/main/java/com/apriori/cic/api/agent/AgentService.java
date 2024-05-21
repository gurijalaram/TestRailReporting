package com.apriori.cic.api.agent;

import com.apriori.shared.util.properties.PropertiesContext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
public class AgentService {
    public static Agent getAgent() {
        Agent agent;
        switch (PropertiesContext.get("ci-connect.agent_type")) {
            case "windchill":
                agent = new WindchillAgent();
                break;
            case "teamcenter":
                agent =  new TeamCenterAgent();
                break;
            case "filesystem":
                agent =  new FileSystemAgent();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + PropertiesContext.get("ci-connect.agent_type"));
        }
        return agent;
    }
}

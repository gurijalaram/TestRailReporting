package entity.response;

import com.apriori.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "AgentConnectionInfoSchema.json")
public class AgentConnectionsInfo {
    private AgentConnectionDataShape dataShape;
    private List<AgentConnectionInfo> rows;
}

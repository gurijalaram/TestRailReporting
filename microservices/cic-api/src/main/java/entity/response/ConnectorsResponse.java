package entity.response;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "AgentConnectorsSchema.json")
public class ConnectorsResponse {
    private ConnectorResponseDataShape dataShape;
    private List<ConnectorInfo> rows;
}
package entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Schema(location = "NexusAgentResponseSchema.json")
public class NexusAgentResponse {
    private List<NexusAgentItem> items;
    private String continuationToken;
}

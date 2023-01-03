package entity.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentWorkflowJobPartCostingInput {
    private String materialName;
    private String processGroupName;
    private String vpeName;
    private Integer annualVolume;
    private Integer batchSize;
    private Double productionLife;
}

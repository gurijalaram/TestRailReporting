package com.apriori.acs.api.models.response.workorders.bomloader;

import com.apriori.acs.api.models.response.workorders.cost.costworkorderstatus.CostStatusCommand;
import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "workorders/BomLoaderSchema.json")
public class BomLoaderStatusInfo {
    private Integer version;
    private String id;
    private String status;
    private String searchKey;
    private BomLoaderCommand command;
    private String dateSubmitted;
    private String dateStarted;
}

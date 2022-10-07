package com.apriori.qds.entity.response.projects;


import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "ProjectItemResponse.json")
public class ProjectItemResponse {
    private String identity;
    private BidPackageProjectItem bidPackageItem;
}

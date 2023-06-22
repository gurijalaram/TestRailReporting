package com.apriori.acs.entity.response.acs.designGuidance;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/DesignGuidanceSheetMetal.json")
public class DesignGuidanceResponse {
    private InfosByTopics infosByTopics;
    private Boolean costingFailed;
}

package com.apriori.acs.entity.response.acs.designGuidance;

import com.apriori.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/DesignGuidanceResponse.json")
public class DesignGuidanceResponse {
    private InfosByTopics infosByTopics;
    private Boolean costingFailed;
}

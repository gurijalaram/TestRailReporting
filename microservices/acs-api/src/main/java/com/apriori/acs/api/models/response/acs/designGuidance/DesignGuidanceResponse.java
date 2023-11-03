package com.apriori.acs.api.models.response.acs.designGuidance;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/DesignGuidanceResponse.json")
public class DesignGuidanceResponse {
    private InfosByTopics infosByTopics;
    private Boolean costingFailed;
}

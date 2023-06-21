package com.apriori.acs.entity.response.acs.designGuidance;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/DesignGuidanceResponse.json")
public class DesignGuidanceResponse {
    private List<InfosByTopics> infosByTopics;
    private Boolean costingFailed;
}

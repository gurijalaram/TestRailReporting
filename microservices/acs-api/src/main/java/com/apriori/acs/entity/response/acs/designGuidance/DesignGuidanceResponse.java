package com.apriori.acs.entity.response.acs.designGuidance;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/DesignGuidanceSheetMetal.json")
public class DesignGuidanceResponse {
    private List<InfosByTopics> infosByTopics;
    private Boolean costingFailed;
}

package com.apriori.edc.api.models.response.reports;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "ReportsResponse.json")
@JsonRootName("response")
@Data
public class ReportsResponse {
    private String identity;
    private String emailIdentity;
}

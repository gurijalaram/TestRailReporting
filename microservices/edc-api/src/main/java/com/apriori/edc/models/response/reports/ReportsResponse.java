package com.apriori.edc.models.response.reports;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "ReportsResponse.json")
@JsonRootName("response")
@Data
public class ReportsResponse {
    private String identity;
    private String emailIdentity;
}

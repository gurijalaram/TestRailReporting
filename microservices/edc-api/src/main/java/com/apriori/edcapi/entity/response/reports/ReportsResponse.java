package com.apriori.edcapi.entity.response.reports;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "ReportsResponse.json")
@JsonRootName("response")
@Data
public class ReportsResponse {
    private String identity;
    private String emailIdentity;
}

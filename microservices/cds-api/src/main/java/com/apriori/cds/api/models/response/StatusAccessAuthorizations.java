package com.apriori.cds.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Schema(location = "StatusAccessAuthorizationsSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class StatusAccessAuthorizations {
    private List<StatusAccessAuthorization> response;
}
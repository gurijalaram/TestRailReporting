package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Schema(location = "StatusAccessAuthorizationsSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class StatusAccessAuthorizations {
    private List<StatusAccessAuthorization> response;
}
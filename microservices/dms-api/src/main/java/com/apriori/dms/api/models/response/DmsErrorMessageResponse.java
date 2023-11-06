package com.apriori.dms.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "DmsErrorMessageResponseSchema.json")
public class DmsErrorMessageResponse {
    private String errorMessage;
}

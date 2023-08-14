package com.apriori.dms.models.response;

import com.apriori.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "DmsErrorMessageResponseSchema.json")
public class DmsErrorMessageResponse {
    private String errorMessage;
}

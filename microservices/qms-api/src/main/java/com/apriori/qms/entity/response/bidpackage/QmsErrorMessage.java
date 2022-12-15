package com.apriori.qms.entity.response.bidpackage;

import com.apriori.utils.ErrorResponse;
import com.apriori.utils.http.enums.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Schema(location = "ErrorMessageSchema.json")
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QmsErrorMessage {
    private String timestamp;
    private Integer status;
    private String method;
    private String error;
    private ArrayList<ErrorResponse> errors;
    private String message;
    private String path;
}

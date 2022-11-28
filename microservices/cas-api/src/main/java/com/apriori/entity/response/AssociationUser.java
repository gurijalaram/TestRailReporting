package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CasAssociationUserSchema.json")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonRootName("response")
public class AssociationUser {
    private String identity;
    private String createdBy;
    private String userIdentity;
    private String createdByName;
}
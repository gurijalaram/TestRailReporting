package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "AssociationUserSchema.json")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AssociationUser {
    private AssociationUser response;
    private String identity;
    private String createdBy;
    private String userIdentity;
    private String createdByName;
}
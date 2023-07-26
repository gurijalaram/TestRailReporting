package com.apriori.entity.response;

import com.apriori.annotations.CreatableModel;
import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CustomerAssociationUserSchema.json")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CreatableModel("userAssociation")
@JsonRootName("response")
public class CustomerAssociationUser {
    private String identity;
    private Date createdAt;
    private String createdBy;
    private String createdByName;
    private Date updatedAt = null;
    private String updatedBy = null;
    private String updatedByName = null;
    private Date deletedAt = null;
    private String deletedBy = null;
    private String deletedByName = null;
    private String userIdentity;
    private CustomerUser user = null;
}

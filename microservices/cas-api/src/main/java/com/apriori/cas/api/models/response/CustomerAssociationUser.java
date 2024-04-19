package com.apriori.cas.api.models.response;

import com.apriori.shared.util.annotations.CreatableModel;
import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.User;

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
    private Date updatedAt;
    private String updatedBy;
    private String updatedByName;
    private Date deletedAt;
    private String deletedBy;
    private String deletedByName;
    private String userIdentity;
    private User user;
}

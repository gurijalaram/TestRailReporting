package com.apriori.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("response")
public class CustomerAssociation {
    private String identity;
    private Date createdAt;
    private String createdBy;
    private String description;
    private String targetCustomerIdentity;
    private String type;

    private Date updatedAt = null;
    private String updatedBy = null;
    private Date deletedAt = null;
    private String deletedBy = null;
}

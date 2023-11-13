package com.apriori.cid.api.utils;

import com.apriori.shared.util.models.request.component.GroupItems;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupCostRequest {
    private String costingTemplateIdentity;
    private List<GroupItems> groupItems;
}
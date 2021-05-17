package com.apriori.cidapp.entity.response.css;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

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
@Schema(location = "cidapp/CssComponentResponse.json")
public class CssComponentResponse extends Pagination {
    private CssComponentResponse response;
    private List<Item> items;
}


package com.apriori.cidapp.entity.response.css;

import com.apriori.apibase.services.common.objects.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@JsonRootName("response")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response extends Pagination {
    private List<Item> items;
}

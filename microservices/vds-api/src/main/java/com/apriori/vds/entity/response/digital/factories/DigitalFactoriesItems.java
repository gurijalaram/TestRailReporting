package com.apriori.vds.entity.response.digital.factories;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "DigitalFactoriesItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class DigitalFactoriesItems extends Pagination {
    private List<DigitalFactory> items;
}

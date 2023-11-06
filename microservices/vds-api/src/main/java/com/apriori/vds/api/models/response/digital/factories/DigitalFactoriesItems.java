package com.apriori.vds.api.models.response.digital.factories;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

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

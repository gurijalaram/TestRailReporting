package com.apriori.sds.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "SecondaryProcessesItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class SecondaryProcessesItems extends Pagination {
    private List<SecondaryProcess> items;
}

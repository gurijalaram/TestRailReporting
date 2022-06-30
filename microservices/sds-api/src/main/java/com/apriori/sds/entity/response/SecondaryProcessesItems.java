package com.apriori.sds.entity.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

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

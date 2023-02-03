package com.apriori.nts.entity.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;


@Schema(location = "NtsGetEmailsItemsResponseSchema.json")
@Data
@JsonRootName("response")
public class EmailsItems extends Pagination {
    private List<Email> items;
}

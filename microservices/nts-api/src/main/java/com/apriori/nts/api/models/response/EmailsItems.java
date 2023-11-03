package com.apriori.nts.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "NtsGetEmailsItemsResponseSchema.json")
@Data
@JsonRootName("response")
public class EmailsItems extends Pagination {
    private List<Email> items;
}

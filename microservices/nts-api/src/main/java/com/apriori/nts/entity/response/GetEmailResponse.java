package com.apriori.nts.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;


@Schema(location = "NtsGetEmailsResponseSchema.json")
@Data
public class GetEmailResponse extends Pagination {
    private GetEmailResponse response;
    private List<Email> items;
}

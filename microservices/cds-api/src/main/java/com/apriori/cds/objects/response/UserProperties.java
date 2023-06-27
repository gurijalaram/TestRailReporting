package com.apriori.cds.objects.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "UserPropertiesSchema.json")
@Data
@JsonRootName("response")
public class UserProperties extends Pagination {
    private List<RequiredProperties> items;
}
package com.apriori.edcapi.entity.response.line.items;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;
@Schema
@Data
public class LineItemsParts {
    private List<LineItemsPart> parts;
}

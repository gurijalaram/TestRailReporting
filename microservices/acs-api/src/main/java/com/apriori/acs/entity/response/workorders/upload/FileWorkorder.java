package com.apriori.acs.entity.response.workorders.upload;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FileWorkorder {
    private String action;
    private List<OrderId> groupItems;
}
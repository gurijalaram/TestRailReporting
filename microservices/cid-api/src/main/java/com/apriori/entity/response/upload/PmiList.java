package com.apriori.entity.response.upload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PmiList {
    private List<PmiItem> PmiItemList;
}

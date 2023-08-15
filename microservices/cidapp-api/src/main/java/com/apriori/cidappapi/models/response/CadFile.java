package com.apriori.cidappapi.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadFile {
    private String filename;
    private String resourceName;
}

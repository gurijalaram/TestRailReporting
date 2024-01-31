package com.apriori.shared.util.nexus.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NexusCheckSum {
    private String sha1;
    private String md5;
}

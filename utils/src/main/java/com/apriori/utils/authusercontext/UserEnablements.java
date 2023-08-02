
package com.apriori.utils.authusercontext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@SuppressWarnings("unused")
public class UserEnablements {
    private Boolean connectAdminEnabled;
    private String createdAt;
    private String createdBy;
    private Boolean highMemEnabled;
    private String identity;
    private Boolean previewEnabled;
    private Boolean sandboxEnabled;
    private String updatedBy;
    private Boolean userAdminEnabled;
}

package com.apriori.cds.api.models.response;

import com.apriori.shared.util.models.response.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SubLicenseAssociationItems extends User {

}
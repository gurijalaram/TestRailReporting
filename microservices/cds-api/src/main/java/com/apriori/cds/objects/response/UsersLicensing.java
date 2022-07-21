package com.apriori.cds.objects.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Schema(location = "UsersLicensingSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UsersLicensing {
    private List<UsersLicense> response;
}
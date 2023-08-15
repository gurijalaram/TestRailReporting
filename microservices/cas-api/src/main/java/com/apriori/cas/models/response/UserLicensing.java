package com.apriori.cas.models.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;

@Schema(location = "UserLicensingSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserLicensing extends ArrayList<UserLicense> {
}
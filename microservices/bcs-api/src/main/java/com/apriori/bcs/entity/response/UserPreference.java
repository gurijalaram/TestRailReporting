package com.apriori.bcs.entity.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.Date;

@Data
@JsonRootName("response")
@Schema(location = "UserPreferenceSchema.json")
public class UserPreference {
    public String identity;
    public Date createdAt;
    public String createdBy;
    public String name;
    public String type;
    public Object value;
    public Date updatedAt;
    public String updatedBy;
}
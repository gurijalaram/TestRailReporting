package main.java.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import main.java.http.enums.Schema;
import main.java.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmss;

import java.time.LocalDateTime;

@Schema(location = "AccountsStatus.json")
@JsonRootName(value = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountsStatus {

    private String identity;

    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmss.class)
    private LocalDateTime licenseStart;

    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmss.class)
    private LocalDateTime licenseEnd;

    private Integer totalRequests;

    private Integer requestsUsed;

    public String getIdentity() {
        return identity;
    }

    public AccountsStatus setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getLicenseStart() {
        return licenseStart;
    }

    public AccountsStatus setLicenseStart(LocalDateTime licenseStart) {
        this.licenseStart = licenseStart;
        return this;
    }

    public LocalDateTime getLicenseEnd() {
        return licenseEnd;
    }

    public AccountsStatus setLicenseEnd(LocalDateTime licenseEnd) {
        this.licenseEnd = licenseEnd;
        return this;
    }

    public Integer getTotalRequests() {
        return totalRequests;
    }

    public AccountsStatus setTotalRequests(Integer totalRequests) {
        this.totalRequests = totalRequests;
        return this;
    }

    public Integer getRequestsUsed() {
        return requestsUsed;
    }

    public AccountsStatus setRequestsUsed(Integer requestsUsed) {
        this.requestsUsed = requestsUsed;
        return this;
    }
}

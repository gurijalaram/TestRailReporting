package com.apriori.trr.api.testrail.exceptions;

import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.Data;

/**
 * Exception representing error returned by TestRail API.
 */
@Data
@Builder
public class TestRailException extends RuntimeException {

    private static final long serialVersionUID = -2131644110724458502L;
    private int responseCode;
    private String error;

    /**
     * @param responseCode the HTTP response code from the TestRail server
     * @param error        the error message from TestRail service
     */
    public TestRailException(int responseCode, String error) {
        super(responseCode + " - " + error);
        this.responseCode = responseCode;
    }

    public TestRailException build() {
        Preconditions.checkNotNull(responseCode);
        Preconditions.checkNotNull(error);
        return new TestRailException(responseCode, error);
    }
}

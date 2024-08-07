package com.apriori.trr.api.testrail;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
    TestRailException(int responseCode, String error) {
        super(responseCode + " - " + error);
        this.responseCode = responseCode;
    }

    public TestRailException build() {
        Preconditions.checkNotNull(responseCode);
        Preconditions.checkNotNull(error);
        return new TestRailException(responseCode, error);
    }
}

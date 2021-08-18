package com.apriori.apibase.services.bcs.objects;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "CisNewReportSchema.json")
public class NewReport extends Report {
    private NewReport response;
    private String roundToNearest;

    public String getRoundToNearest() {
        return this.roundToNearest;
    }

    public NewReport setRoundToNearest(String scenarioIterationKey) {
        this.roundToNearest = roundToNearest;
        return this;
    }

    public NewReport getResponse() {
        return this.response;
    }

    public NewReport setResponse(NewReport response) {
        this.response = response;
        return this;
    }

}

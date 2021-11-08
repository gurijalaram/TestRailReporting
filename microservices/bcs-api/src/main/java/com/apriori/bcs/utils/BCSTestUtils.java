package com.apriori.bcs.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.entity.request.NewBatchProperties;
import com.apriori.bcs.entity.request.NewPartRequest;
import com.apriori.bcs.entity.request.NewReportRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.entity.response.Report;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

import lombok.Data;
import org.apache.http.HttpStatus;

@Data
public class BCSTestUtils extends TestUtil {
    private String batchIdentity;
    private int batchStatus;
    private String batchState;
    private boolean batchCompleted;
    private Class batchClass;
    private Batch batch;
    private ResponseWrapper<Batch> batchResponse;
    private String partIdentity;
    private int partStatus;
    private String partState;
    private boolean partCompleted;
    private Class partClass;
    private Part part;
    private ResponseWrapper<Part> partResponse;
    private NewPartRequest newPartRequest;
    private Report newReport;
    private NewReportRequest newReportRequest;
    private NewBatchProperties newBatchProperties;

    private static BCSTestUtils bcsTestUtils;

    static {
        bcsTestUtils = new BCSTestUtils();
    }

    public BCSTestUtils() {
        super();
    }

    /**
     * Create a new part request with default values
     *
     * @return
     */
    public BCSTestUtils generateNewPartRequest() {
        NewPartRequest newPartRequest =
                (NewPartRequest) JsonManager.deserializeJsonFromInputStream(
                        FileResourceUtil.getResourceFileStream(
                                "schemas/requests/CreatePartData.json"),
                        NewPartRequest.class);
        this.newPartRequest = newPartRequest;
        return this;
    }

    /**
     * Add a part to the specified batch
     *
     * @param batchIdentity batch identity
     * @return
     */
    public BCSTestUtils addPartToBatch(String batchIdentity) {
        ResponseWrapper<Part> part = BatchPartResources.createNewBatchPart(newPartRequest,
                batchIdentity);
        this.partIdentity = part.getResponseEntity().getIdentity();
        this.partStatus = part.getStatusCode();
        this.part = part.getResponseEntity();
        this.partResponse = part;
        return this;
    }

    public BCSTestUtils addPartToBatch() {
        addPartToBatch(batchIdentity);
        return this;
    }

    /**
     * Creates a new batch and adds a part
     *
     * @return instance of this class
     */
    public BCSTestUtils createBatchPart() {
        Batch batch = BatchResources.createNewBatch();
        this.batchIdentity = batch.getIdentity();
        generateNewPartRequest();
        addPartToBatch();
        return this;
    }

    /**
     * sets common batch & part properties
     *
     * @return
     */
    public BCSTestUtils setBatchPartProperties() {
        setPartStatus(HttpStatus.SC_CREATED);
        setPartClass(Part.class);
        setBatchClass(Batch.class);
        setNewBatchProperties(BcsUtils.generateNewBatchProperties());
        setBatchStatus(HttpStatus.SC_CREATED);

        return this;
    }


}

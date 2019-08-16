package main.java.api.edc.util;

import main.java.http.builder.common.response.common.*;
import main.java.http.builder.service.HTTPRequest;
import main.java.http.enums.common.api.BillOfMaterialsAPIEnum;
import main.java.http.enums.common.api.PartsAPIEnum;
import main.java.utils.MultiPartFiles;
import main.java.utils.WebDriverUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.*;


public class UserTestDataUtil {

    private static final Logger logger = LoggerFactory.getLogger(UserTestDataUtil.class);

    private static BlockingQueue<List<String>> usersQueue = new LinkedBlockingQueue<>();

    static {
        recordUsersForTest();
    }

    private static void recordUsersForTest() {
        final File usersCredsCSV = getResourceFile("test-data/edc-qa-users.csv");

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(usersCredsCSV))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                usersQueue.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            logger.error(String.format("Error with initializing users. Users file: %s", usersCredsCSV.getAbsolutePath()));
            throw new IllegalArgumentException();
        }
    }

    private static File getResourceFile(String resourceFileName) {
        try {
            return new File(
                    URLDecoder.decode(
                            ClassLoader.getSystemResource(resourceFileName).getFile(),
                            "UTF-8"
                    )
            );
        } catch (UnsupportedEncodingException e) {
            logger.error(String.format("Resource file: %s was not fount", resourceFileName));
            throw new IllegalArgumentException();
        }
    }

    private List<String> getUserCredentials() {
        try {
            return usersQueue.take();
        } catch (InterruptedException e) {
            logger.error("Can't take user from queue. Thread info:" + Thread.currentThread().getName());
            throw new IllegalArgumentException();
        }
    }

    public UserDataEDC initBillOfMaterials() {
        List<String> userNamePass = getUserCredentials();

        UserDataEDC userDataEDC = new UserDataEDC(userNamePass.get(0), userNamePass.get(1));

        userDataEDC.setTokenAndInitAuthorizationHeaders(
                new WebDriverUtils()
                        .getToken(userNamePass.get(0),
                                userNamePass.get(1)
                        )
        );

        uploadTestData(userDataEDC);

        userDataEDC.setBillOfMaterials(
                getBillOfMaterials(
                        userDataEDC.getAuthorizationHeaders()
                ).getBillOfMaterialsList()
        );

        userDataEDC.setBillOfMaterial(
                getBillOfMaterial(userDataEDC.getBillOfMaterials())
        );

        userDataEDC.setLineItem(
                getRandomLineItemWithParts(getMaterialsLineItemWrapper(userDataEDC))
        );

        userDataEDC.setMaterialPart(
                userDataEDC.getLineItem().getMaterialParts().get(0)
                        .setUserPart(true)
                        .setAverageCost(1f)
                        .setManufacturerPartNumber(userDataEDC.getLineItem().getManufacturerPartNumber())
        );

        return userDataEDC;
    }

    private MaterialsLineItemsWrapper getMaterialsLineItemWrapper(UserDataEDC userDataEDC) {
        return (MaterialsLineItemsWrapper) new HTTPRequest()
                    .unauthorized()
                    .customizeRequest()
                    .setInlineVariables(userDataEDC.getBillOfMaterial().getIdentity())
                    .setHeaders(userDataEDC.getAuthorizationHeaders())
                    .setEndpoint(PartsAPIEnum.GET_LINE_ITEMS)
                    .setReturnType(MaterialsLineItemsWrapper.class)
                    .commitChanges()
                    .connect()
                    .get();
    }

    private BillOfMaterial getBillOfMaterial(List<BillOfMaterial> billOfMaterials) {
        return billOfMaterials.get(
                new Random().nextInt(
                        billOfMaterials.size()
                )
        );
    }

    private BillOfMaterialsWrapper getBillOfMaterials(Map<String, String> authorizationHeaders) {
        return (BillOfMaterialsWrapper) new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS)
                .setReturnType(BillOfMaterialsWrapper.class)
                .setHeaders(authorizationHeaders)
                .commitChanges()
                .connect()
                .get();
    }

    public void clearTestData(final UserDataEDC userDataEDC) {
        userDataEDC.getBillOfMaterials().forEach(billOfMaterial -> new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setHeaders(userDataEDC.getAuthorizationHeaders())
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS_IDENTITY)
                .setInlineVariables(billOfMaterial.getIdentity())
                .setStatusCode(204)
                .commitChanges()
                .connect()
                .delete());
    }

    public void uploadTestData(final UserDataEDC userDataEDC) {

        final File testData = getResourceFile("test-data/apriori-3-items.csv");

        new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setHeaders(userDataEDC.getAuthorizationHeaders())
                .setMultiPartFiles(
                        new MultiPartFiles().use("multiPartFile", testData)
                )
                .setStatusCode(201)
                .setEndpoint(BillOfMaterialsAPIEnum.POST_BILL_OF_METERIALS)
                .commitChanges()
                .connect()
                .postMultiPart();
    }

    private MaterialLineItem getRandomLineItemWithParts(MaterialsLineItemsWrapper materialsLineItemsWrapper) {
        MaterialLineItem materialLineItem = materialsLineItemsWrapper.getMaterialLineItems()
                .get(new Random().nextInt(materialsLineItemsWrapper.getMaterialLineItems().size()));

        if (materialLineItem.getMaterialParts().size() == 0) {
            return getRandomLineItemWithParts(materialsLineItemsWrapper);
        }

        return materialLineItem;
    }
}

package com.apriori.cidappapi.utils;

import com.apriori.FileResourceUtil;
import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.request.ScenarioIterationRequest;
import com.apriori.enums.CssAPIEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.request.ErrorRequestResponse;
import com.apriori.models.response.CssComponentResponse;
import com.apriori.models.response.ScenarioItem;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ScenarioIterationService {

    private UserCredentials currentUser;
    private final ComponentsUtil componentsUtil = new ComponentsUtil();

    public ScenarioIterationService() {
        currentUser = UserUtil.getUser();
    }

    /**
     * calls 'scenario-iterations' GET endpoint
     *
     * @param queryParams - pass parameters to the curl
     */

    public ResponseWrapper<CssComponentResponse> getScenarioIterationWithParams(QueryParams queryParams) {

        RequestEntity requestEntity =
            RequestEntityUtil.init(CssAPIEnum.SCENARIO_ITERATIONS, CssComponentResponse.class)
                .token(currentUser.getToken())
                .queryParams(queryParams);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * calls 'scenario-iterations' POST endpoint - for error responses
     *
     * @param scenarioIterationRequest - pass the body
     */
    public ResponseWrapper<ErrorRequestResponse> getScenarioIterationWithParamsPostForErrors(ScenarioIterationRequest scenarioIterationRequest) {

        RequestEntity requestEntity =
            RequestEntityUtil.init(CssAPIEnum.SCENARIO_ITERATIONS_QUERY, ErrorRequestResponse.class)
                .body(scenarioIterationRequest)
                .token(currentUser.getToken());
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * calls 'scenario-iterations' POST endpoint
     *
     * @param scenarioIterationRequest - pass the body
     */
    public ResponseWrapper<CssComponentResponse> getScenarioIterationWithParamsPost(ScenarioIterationRequest scenarioIterationRequest) {

        RequestEntity requestEntity =
            RequestEntityUtil.init(CssAPIEnum.SCENARIO_ITERATIONS_QUERY, ErrorRequestResponse.class)
                .body(scenarioIterationRequest)
                .token(currentUser.getToken());
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * runs in @BeforeAll method to ensure that data is loaded before tests startsv
     * (especially when running on local env)
     */
    public void loadDataIfNotExists() {
        Map<String, String> dataMap = new HashMap();
        dataMap.put("bracket_basic.prt", "Sheet Metal - Stretch Forming");
        dataMap.put("case_002_006-8611543_prt.stp", "Stock Machining");
        dataMap.put("oldham.asm.1", "Assembly");
        dataMap.put("ms16555-627_1.prt.1", "Stock Machining");
        dataMap.put("PowderMetalShaft.stp", "Powder Metal");
        dataMap.put("BasicScenario_Forging.stp", "Forging");
        dataMap.put("prt0001.prt.1", "Stock Machining");
        dataMap.put("extremebends.prt.1", "Sheet Metal");
        dataMap.put("CurvedWall.CATPart", "Casting");
        dataMap.put("MultiUpload.stp", "Plastic Molding");
        dataMap.put("case_066_SpaceX_00128711-001_A.stp", "Stock Machining");


        Iterator<Map.Entry<String, String>> iterator = dataMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            ifNotExistsLoad(currentUser, entry);
        }
    }

    private void ifNotExistsLoad(UserCredentials currentUser, Map.Entry<String, String> entry) {
        if (!ifExists(entry)) {
            String componentName = entry.getKey();
            String[] component = componentName.split("\\.", 2);
            ProcessGroupEnum pg = ProcessGroupEnum.fromString(entry.getValue());
            File resourceFile = FileResourceUtil.getCloudFile(pg, component[0] + "." + component[1]);
            String scenarioName = ("AutoAPI" + entry.getValue() + component[0]).replace(" ", "");

            componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile)
                .user(currentUser)
                .build());
        }
    }

    private boolean ifExists(Map.Entry<String, String> entry) {
        String[] component = entry.getKey().split("\\.", 2);
        String searchedItem = component[0];
        String property = "componentName[EQ]";

        QueryParams queryParams = new QueryParams();
        queryParams.use(property, searchedItem);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond =
            getScenarioIterationWithParams(queryParams);
        if (scenarioIterationRespond.getResponseEntity().getItems().size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * assertion method used in tests
     */

    public boolean validateIfTrue(ResponseWrapper<CssComponentResponse> scenarioIterationRespond) {
        for (ScenarioItem item : scenarioIterationRespond.getResponseEntity().getItems()) {
            if (item.getComponentType().equals("PART") || item.getComponentType().equals("ASSEMBLY")) {
                continue;
            } else {
                log.error("item: " + item.getComponentName() + " does not meet test criteria");
                return false;
            }
        }
        return true;
    }

    /**
     * assertion method used in tests
     */

    public boolean validateIfTrueWithAndOperator(ResponseWrapper<CssComponentResponse> scenarioIterationRespond) {
        for (ScenarioItem item : scenarioIterationRespond.getResponseEntity().getItems()) {
            if (item.getComponentType().equals("PART") && item.getComponentName().equals("bracket_basic")) {
                continue;
            } else {
                log.error("item: " + item.getComponentName() + " does not meet test criteria");
                return false;
            }
        }
        return true;
    }

    /**
     * assertion method used in tests
     */

    public boolean validateIfTrueWithSwOperator(ResponseWrapper<CssComponentResponse> scenarioIterationRespond) {
        Pattern p = Pattern.compile("BR.*", Pattern.CASE_INSENSITIVE);

        for (ScenarioItem item : scenarioIterationRespond.getResponseEntity().getItems()) {
            Matcher m = p.matcher(item.getComponentName());
            if (m.matches()) {
                continue;
            } else {
                log.error("item: " + item.getComponentName() + " does not meet test criteria");
                return false;
            }
        }
        return true;
    }
}

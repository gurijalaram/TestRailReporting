package com.apriori.css.entity.apicalls;

import com.apriori.css.entity.builder.ComponentInfoBuilder;
import com.apriori.css.entity.enums.CssAPIEnum;
import com.apriori.css.entity.request.ErrorRequestResponse;
import com.apriori.css.entity.request.ScenarioIterationRequest;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.css.utils.ComponentsUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

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
    private final  ComponentsUtil componentsUtil = new ComponentsUtil();

    public ScenarioIterationService() {
        currentUser = UserUtil.getUser();
    }

    /**
     * calls 'scenario-iterations' GET endpoint
     *
     * @param formParams - pass parameters to the curl
     */

    public ResponseWrapper getScenarioIterationWithParams(FormParams formParams) {

        RequestEntity requestEntity =
                RequestEntityUtil.init(CssAPIEnum.COMPONENT_SCENARIO_QUERY, CssComponentResponse.class)
                        .token(currentUser.getToken())
                    .formParams(formParams);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * calls 'scenario-iterations' POST endpoint - for error responses
     *
     * @param scenarioIterationRequest - pass the body
     */
    public ResponseWrapper getScenarioIterationWithParamsPostForErrors(ScenarioIterationRequest scenarioIterationRequest) {

        RequestEntity requestEntity =
                RequestEntityUtil.init(CssAPIEnum.COMPONENT_SCENARIO_QUERY_NEW, ErrorRequestResponse.class)
                    .body(scenarioIterationRequest)
                        .token(currentUser.getToken());
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * calls 'scenario-iterations' POST endpoint
     *
     * @param scenarioIterationRequest - pass the body
     */
    public ResponseWrapper getScenarioIterationWithParamsPost(ScenarioIterationRequest scenarioIterationRequest) {

        RequestEntity requestEntity =
            RequestEntityUtil.init(CssAPIEnum.COMPONENT_SCENARIO_QUERY_NEW, CssComponentResponse.class)
                .body(scenarioIterationRequest)
                .token(currentUser.getToken());
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * runs in @BeforeClass method to ensure that data is loaded before tests startsv
     * (especially when running on local env)
     */
    public void loadDataIfNotExists() {
        Map<String,String> dataMap = new HashMap();
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

            componentsUtil.postComponentQueryCSS(ComponentInfoBuilder.builder()
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

        FormParams formParams = new FormParams();
        formParams.use(property, searchedItem);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond =
                getScenarioIterationWithParams(formParams);
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
        Pattern p = Pattern.compile("BR.*",Pattern.CASE_INSENSITIVE);

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

package utils;

import com.apriori.utils.FileResourceUtil;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class Constants {

    public static final String LOGOUT_HEADER = "CI DESIGN AUTOMATION";
    public static final String REPORTS_LAST_SUFFIX = "flow.html?_flowId=homeFlow";
    public static final String CIA_USER_GUIDE_URL_SUBSTRING = "CI_ADMIN_USER_GUIDE";
    public static final String SCENARIO_EXPORT_CHAPTER_URL_PART_ONE = "https://www.apriori.com/Collateral/Documents/English-US/online_help/apriori-platform/";
    public static final String SCENARIO_EXPORT_CHAPTER_URL_PART_TWO = "CIA_UG";
    public static final String CIA_USER_GUIDE_TITLE = "Cost Insight Admin\nUser Guide";
    public static final String SCENARIO_EXPORT_CHAPTER_PAGE_TITLE = "2 Scenario and System Data Exports";
    public static final String REPORTS_URL_SUFFIX = "jasperserver-pro/";
    public static final String DEFAULT_SCENARIO_NAME = "Initial";
    public static final String ON_PREM_BASE_URL = "https://conqbaci";
    public static final String REPORTS_LOGIN_LOCAL_SUFFIX = "jasperserver-pro/login.html";
}

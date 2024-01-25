package com.apriori.shared.util.nexus.utils;

import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtilBuilder;
import com.apriori.shared.util.nexus.enums.NexusAPIEnum;
import com.apriori.shared.util.nexus.models.response.NexusAgentItem;
import com.apriori.shared.util.nexus.models.response.NexusAgentResponse;
import com.apriori.shared.util.properties.PropertiesContext;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class NexusUtil {

    private static RequestEntityUtil requestEntityUtil;
    private static NexusAgentResponse nexusAgentResponse;
    private static NexusComponent nexusComponentData;
    private static NexusAgentItem nexusAgentItem;
    private static String credential;

    /**
     * search repository by group name and version for specific file extenstion
     *
     * @param nexusSearchParameters - NexusSearchParameters
     * @return NexusAgentItem
     */
    public static NexusAgentItem searchRepositoryByGroup(NexusSearchParameters nexusSearchParameters) {
        init();
        RequestEntity requestEntity;
        String group = "/" + nexusSearchParameters.getGroupName() + "/" + nexusSearchParameters.getVersion();
        try {
            requestEntity = requestEntityUtil.init(NexusAPIEnum.NEXUS_CIC_AGENT_SEARCH_BY_GROUP, NexusAgentResponse.class)
                .inlineVariables(nexusSearchParameters.getRepositoryName(), group)
                .headers(new HashMap<>() {
                    {
                        put("Authorization", "Basic " + Base64.getEncoder().encodeToString(credential.getBytes()));
                    }
                }).expectedResponseCode(HttpStatus.SC_OK);

            nexusAgentResponse = (NexusAgentResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
        } catch (NullPointerException nullPointerException) {
            log.error(nullPointerException.getMessage() + "REPOSITORY NOT FOUND IN NEXUS - " + group);
            throw new IllegalArgumentException(nullPointerException);
        }

        if (nexusAgentResponse.getContinuationToken() == null) {
            nexusAgentItem = nexusAgentResponse.getItems().stream()
                .filter(wf -> wf.getName().endsWith(nexusSearchParameters.getFileExtension()))
                .findFirst()
                .orElse(null);
        } else {
            nexusAgentItem = recursiveSearch(nexusSearchParameters, nexusAgentResponse.getContinuationToken());
        }
        return nexusAgentItem;
    }

    /**
     * download component from repository
     *
     * @return NexusComponent
     */
    public static NexusComponent downloadComponent() {
        try {
            nexusComponentData.setBaseFolder(String.valueOf(FileResourceUtil.createTempDir(null)).toLowerCase());
            nexusComponentData.setAgentZipFolder(nexusComponentData.getBaseFolder() + File.separator + StringUtils.substringAfterLast(nexusAgentItem.getName(), "/"));
            nexusComponentData.setAgentUnZipFolder(nexusComponentData.getBaseFolder() + File.separator + FilenameUtils.removeExtension(StringUtils.substringAfterLast(nexusAgentItem.getName(), "/")));
        } catch (Exception ioException) {
            log.error("PATH NOT FOUND!!");
        }

        if (Boolean.valueOf(PropertiesContext.get("ci-connect.custom_install"))) {
            NexusAPIEnum.NEXUS_CIC_AGENT_DOWNLOAD_URL.setEndpoint(nexusAgentItem.getAssets().get(0).getDownloadUrl());
            downloadAgent(NexusAPIEnum.NEXUS_CIC_AGENT_DOWNLOAD_URL);
        } else {
            String lastModifiedDate = nexusAgentItem.getAssets().get(0).getLastModified().split("T")[0];
            if (LocalDate.now().minusDays(8).isAfter(LocalDate.parse(lastModifiedDate))) {
                log.info("AGENT INSTALLER IS NOT UPDATED " + nexusAgentItem.getName());
                System.exit(0);
            } else {
                NexusAPIEnum.NEXUS_CIC_AGENT_DOWNLOAD_URL.setEndpoint(nexusAgentItem.getAssets().get(0).getDownloadUrl());
                downloadAgent(NexusAPIEnum.NEXUS_CIC_AGENT_DOWNLOAD_URL);
            }
        }
        return nexusComponentData;
    }

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     */
    public static void extractZip() {
        ZipInputStream zipIn = null;
        try {
            File destDir = new File(nexusComponentData.getAgentUnZipFolder());
            if (!destDir.exists()) {
                destDir.mkdir();
            } else {
                FileUtils.cleanDirectory(destDir);
            }
            zipIn = new ZipInputStream(new FileInputStream(nexusComponentData.getAgentZipFolder()));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                String filePath = nexusComponentData.getAgentUnZipFolder() + File.separator + entry.getName();
                log.info(String.format("########## DOWNLOADED AGENT FOLDER --%s ###############", filePath));
                if (!entry.isDirectory()) {
                    // if the entry is a file, extracts it
                    extractFile(zipIn, filePath);
                } else {
                    // if the entry is a directory, make the directory
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch (IOException ioException) {
            log.error(String.valueOf(ioException), ioException);
        }
    }

    /**
     * Initialization of request entity and credentials
     */
    private static void init() {
        requestEntityUtil = RequestEntityUtilBuilder.useRandomUser("admin");
        credential = PropertiesContext.get("global.nexus.username") + ":" + PropertiesContext.get("global.nexus.password");
        nexusComponentData = new NexusComponent();
    }

    /**
     * Search for specific version of file continuously until it is found
     *
     * @param nexusSearchParameters - NexusSearchParameters
     * @return NexusAgentItem
     */
    private static NexusAgentItem recursiveSearch(NexusSearchParameters nexusSearchParameters, String continuationToken) {
        RequestEntity requestEntity = null;
        String group = "/" + nexusSearchParameters.getGroupName() + "/" + nexusSearchParameters.getVersion();
        try {
            requestEntity = requestEntityUtil.init(NexusAPIEnum.NEXUS_CIC_AGENT_SEARCH_BY_GROUP_CONTINUATION, NexusAgentResponse.class)
                .inlineVariables(nexusSearchParameters.getRepositoryName(), group, continuationToken)
                .headers(new HashMap<String, String>() {
                    {
                        put("Authorization", "Basic " + Base64.getEncoder().encodeToString(credential.getBytes()));
                    }
                }).expectedResponseCode(HttpStatus.SC_OK);
            nexusAgentResponse = (NexusAgentResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
            nexusAgentItem = nexusAgentResponse.getItems().stream()
                .filter(wf -> wf.getName().endsWith(nexusSearchParameters.getFileExtension()))
                .findFirst()
                .orElse(null);

            if (nexusAgentItem != null) {
                return nexusAgentItem;
            }

            if (nexusAgentResponse.getContinuationToken() != "null") {
                recursiveSearch(nexusSearchParameters, nexusAgentResponse.getContinuationToken());
            }

        } catch (Exception e) {
            log.error("AGENT INSTALLER NOT FOUND IN NEXUS REPOSITORY!!");
        }

        return nexusAgentItem;
    }

    /**
     * Download agent from nexus download api url
     *
     * @param agentUrl NexusAPIEnum
     */
    private static void downloadAgent(NexusAPIEnum agentUrl) {
        InputStream inputStream = null;
        try {
            File agentZipFile = new File(nexusComponentData.getAgentZipFolder());
            if (agentZipFile.exists()) {
                agentZipFile.delete();
            }
            inputStream = RestAssured.given()
                .headers(new HashMap<String, String>() {
                    {
                        put("Authorization", "Basic " + Base64.getEncoder().encodeToString(credential.getBytes()));
                    }
                })
                .config(RestAssuredConfig.config()
                    .httpClient(
                        HttpClientConfig.httpClientConfig()
                            .setParam("http.connection.timeout", 60000)
                            .setParam("http.socket.timeout", 60000)
                    ))
                .get(agentUrl.NEXUS_CIC_AGENT_DOWNLOAD_URL.getEndpointString())
                .asInputStream();

            FileUtils.copyInputStreamToFile(inputStream, new File(nexusComponentData.getAgentZipFolder()));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error("FAILED TO DOWNLOAD THE AGENT INSTALLER!!!!");
        }
    }

    /**
     * Extracts a zip entry (file entry)
     *
     * @param zipIn
     * @param filePath
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            byte[] bytesIn = new byte[4096];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
            bos.close();
        } catch (IOException ioException) {
            log.error(String.valueOf(ioException), ioException);
        }
    }
}

package com.apriori.cic.agent.utils;

import com.apriori.shared.util.http.utils.EncryptionUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.json.JsonManager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentCredentials {
    private String host;
    private String port;
    private String username;
    private String password;
    private String privateKey;
    private String plmUser;
    private String plmPassword;

    /**
     * Retrieve credentials and host url for the email account.
     */
    @SneakyThrows
    public AgentCredentials getAgentCredentials() {
        String key = "lygtvxdsesdfhind";
        InputStream credentialFile = FileResourceUtil.getResourceFileStream("agentCredentials");
        String content = EncryptionUtil.decryptFile(key, credentialFile);
        return JsonManager.deserializeJsonFromString(content, AgentCredentials.class);
    }
}

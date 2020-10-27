package com.apriori.utils.constants;

import org.aeonbits.owner.Config;

@Config.Sources({
    "classpath:${env}/${env}.properties"
})
public interface ConstantsInit extends Config {

    @Key("url.default")
    String url();

    @Key("schema.base.path")
    String schemaBasePath();

    @Key("url.additional.internal.api")
    String internalApiURL();

    @Key("url.additional.cid")
    String cidURL();

    @Key("url.additional.cir")
    String cirURL();

    @Key("url.additional.admin")
    String ciaURL();

    @Key("url.additional.cic")
    String cicURL();

    @Key("url.additional.cidapp")
    String cidAppURL();

    @Key("logout.header.text")
    String logoutHeaderText();

    @Key("url.grid.server")
    String gridServerUrl();

    @Key("console.log.level")
    String consoleLogLevelData();

    @Key("users.csv.file")
    String usersCsvFileName();

    @Key("different.users")
    Boolean useDifferentUsers();

    @Key("service.host")
    String serviceHost();

    @Key("service.port")
    String servicePort();

    @Key("service.name")
    String serviceName();

    @Key("secret.key")
    String secretKey();

    @Key("cds.identity.role")
    String cdsIdentityRole();

    @Key("cds.identity.user")
    String cdsIdentityUser();

    @Key("cds.identity.customer")
    String cdsIdentityCustomer();

    @Key("cds.identity.application")
    String cdsIdentityApplication();

    @Key("ats.service.host")
    String atsServiceHost();

    @Key("ats.token.username")
    String atsTokenUsername();

    @Key("ats.token.email")
    String atsTokenEmail();

    @Key("ats.token.issuer")
    String atsTokenIssuer();

    @Key("ats.token.subject")
    String atsTokenSubject();

    @Key("ats.auth.application")
    String atsAuthApplication();

    @Key("ats.auth.targetCloudContext")
    String atsAuthTargetCloudContext();

    @Key("fms.service.host")
    String fmsServiceHost();

    @Key("fms.file.identity")
    String fmsFileIdentity();

    @Key("cis.customer.identity")
    String cisCustomerIdentity();

    @Key("cis.service.host")
    String cisServiceHost();

    @Key("cis.part.identity")
    String cisPartIdentity();

    @Key("cis.report.identity")
    String cisReportIdentity();

    @Key("cis.reportType.identity")
    String cisReportTypeIdentity();

    @Key("cis.batch.identity")
    String cisBatchIdentity();

    @Key("apitests.base.path")
    String apitestsBasePath();

    @Key("apitests.resource.path")
    String apitestsResourcePath();

    @Key("nts.auth.targetCloudContext")
    String ntsTargetCloudContext();

    @Key("nts.service.host")
    String ntsServiceHost();

    @Key("nts.email.recipientAddress")
    String ntsEmailRecipientAddress();

    @Key("nts.email.subject")
    String ntsEmailSubject();

    @Key("nts.email.content")
    String ntsEmailContent();

    @Key("nts.email.attachment")
    String ntsEmailAttachment();
}

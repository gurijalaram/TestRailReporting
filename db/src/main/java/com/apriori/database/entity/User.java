package com.apriori.database.entity;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "fbc_user")
public class User implements Serializable {

    private static final Long serialVersionUID = -231312312L;

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_ID")
    private Long userId;
    @Column(name = "rawLoginID")
    private String rawLoginID;
    @Column(name = "fullName")
    private String fullName;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "middleName")
    private String middleName;
    @Column(name = "password")
    private String password;
    @Column(name = "siteId")
    private String siteId;
    @Column(name = "licenseId")
    private String licenseId;
    @Column(name = "preferredPlantName")
    private String preferredPlantName;
    @Column(name = "signatureData")
    private String signatureData;
    @Column(name = "location")
    private String location;
    @Column(name = "department")
    private String department;
    @Column(name = "\"function\"")
    // because "function" is reserved word in SQL, we have to put backslash before column name
    private String function;
    @Column(name = "manager")
    private String manager;
    @Column(name = "email")
    private String email;
    @Column(name = "provenance")
    private String provenance;
    @Column(name = "resetPassword")
    private Byte resetPassword;
    @Column(name = "defaultDeploymentUuid")
    private String defaultDeploymentUuid;
    @Column(name = "defaultScenarioSchemaUuid")
    private String defaultScenarioSchemaUuid;
    @Column(name = "lengthUnitSymbol")
    private String lengthUnitSymbol;
    @Column(name = "massUnitSymbol")
    private String massUnitSymbol;
    @Column(name = "forceUnitSymbol")
    private String forceUnitSymbol;
    @Column(name = "angleUnitSymbol")
    private String angleUnitSymbol;
    @Column(name = "timeUnitSymbol")
    private String timeUnitSymbol;
    @Column(name = "temperatureUnitSymbol")
    private String temperatureUnitSymbol;
    @Column(name = "energyUnitSymbol")
    private String energyUnitSymbol;
    @Column(name = "currencyCode")
    private String currencyCode;
    @Column(name = "userDataXML")
    private String userDataXML;
    @Column(name = "defaultRole")
    private String defaultRole;
    @Column(name = "extra1")
    private String extra1;
    @Column(name = "extra2")
    private String extra2;
    @Column(name = "extra3")
    private String extra3;
    @Column(name = "extra4")
    private String extra4;
    @Column(name = "extra5")
    private String extra5;
    @Column(name = "extra6")
    private String extra6;
    @Column(name = "extra7")
    private String extra7;
    @Column(name = "extra8")
    private String extra8;
    @Column(name = "extra9")
    private String extra9;
    @Column(name = "extra10")
    private String extra10;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserGroups> userGroups;

    public static Long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getUserId() {
        return userId;
    }

    public User setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getRawLoginID() {
        return rawLoginID;
    }

    public User setRawLoginID(String rawLoginID) {
        this.rawLoginID = rawLoginID;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public User setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public User setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSiteId() {
        return siteId;
    }

    public User setSiteId(String siteId) {
        this.siteId = siteId;
        return this;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public User setLicenseId(String licenseId) {
        this.licenseId = licenseId;
        return this;
    }

    public String getPreferredPlantName() {
        return preferredPlantName;
    }

    public User setPreferredPlantName(String preferredPlantName) {
        this.preferredPlantName = preferredPlantName;
        return this;
    }

    public String getSignatureData() {
        return signatureData;
    }

    public User setSignatureData(String signatureData) {
        this.signatureData = signatureData;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public User setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getDepartment() {
        return department;
    }

    public User setDepartment(String department) {
        this.department = department;
        return this;
    }

    public String getFunction() {
        return function;
    }

    public User setFunction(String function) {
        this.function = function;
        return this;
    }

    public String getManager() {
        return manager;
    }

    public User setManager(String manager) {
        this.manager = manager;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getProvenance() {
        return provenance;
    }

    public User setProvenance(String provenance) {
        this.provenance = provenance;
        return this;
    }

    public Byte getResetPassword() {
        return resetPassword;
    }

    public User setResetPassword(Byte resetPassword) {
        this.resetPassword = resetPassword;
        return this;
    }

    public String getDefaultDeploymentUuid() {
        return defaultDeploymentUuid;
    }

    public User setDefaultDeploymentUuid(String defaultDeploymentUuid) {
        this.defaultDeploymentUuid = defaultDeploymentUuid;
        return this;
    }

    public String getDefaultScenarioSchemaUuid() {
        return defaultScenarioSchemaUuid;
    }

    public User setDefaultScenarioSchemaUuid(String defaultScenarioSchemaUuid) {
        this.defaultScenarioSchemaUuid = defaultScenarioSchemaUuid;
        return this;
    }

    public String getLengthUnitSymbol() {
        return lengthUnitSymbol;
    }

    public User setLengthUnitSymbol(String lengthUnitSymbol) {
        this.lengthUnitSymbol = lengthUnitSymbol;
        return this;
    }

    public String getMassUnitSymbol() {
        return massUnitSymbol;
    }

    public User setMassUnitSymbol(String massUnitSymbol) {
        this.massUnitSymbol = massUnitSymbol;
        return this;
    }

    public String getForceUnitSymbol() {
        return forceUnitSymbol;
    }

    public User setForceUnitSymbol(String forceUnitSymbol) {
        this.forceUnitSymbol = forceUnitSymbol;
        return this;
    }

    public String getAngleUnitSymbol() {
        return angleUnitSymbol;
    }

    public User setAngleUnitSymbol(String angleUnitSymbol) {
        this.angleUnitSymbol = angleUnitSymbol;
        return this;
    }

    public String getTimeUnitSymbol() {
        return timeUnitSymbol;
    }

    public User setTimeUnitSymbol(String timeUnitSymbol) {
        this.timeUnitSymbol = timeUnitSymbol;
        return this;
    }

    public String getTemperatureUnitSymbol() {
        return temperatureUnitSymbol;
    }

    public User setTemperatureUnitSymbol(String temperatureUnitSymbol) {
        this.temperatureUnitSymbol = temperatureUnitSymbol;
        return this;
    }

    public String getEnergyUnitSymbol() {
        return energyUnitSymbol;
    }

    public User setEnergyUnitSymbol(String energyUnitSymbol) {
        this.energyUnitSymbol = energyUnitSymbol;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public User setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }


    public String getUserDataXML() {
        return userDataXML;
    }

    public User setUserDataXML(String userDataXML) {
        this.userDataXML = userDataXML;
        return this;
    }

    public String getDefaultRole() {
        return defaultRole;
    }

    public User setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
        return this;
    }

    public String getExtra1() {
        return extra1;
    }

    public User setExtra1(String extra1) {
        this.extra1 = extra1;
        return this;
    }

    public String getExtra2() {
        return extra2;
    }

    public User setExtra2(String extra2) {
        this.extra2 = extra2;
        return this;
    }

    public String getExtra3() {
        return extra3;
    }

    public User setExtra3(String extra3) {
        this.extra3 = extra3;
        return this;
    }

    public String getExtra4() {
        return extra4;
    }

    public User setExtra4(String extra4) {
        this.extra4 = extra4;
        return this;
    }

    public String getExtra5() {
        return extra5;
    }

    public User setExtra5(String extra5) {
        this.extra5 = extra5;
        return this;
    }

    public String getExtra6() {
        return extra6;
    }

    public User setExtra6(String extra6) {
        this.extra6 = extra6;
        return this;
    }

    public String getExtra7() {
        return extra7;
    }

    public User setExtra7(String extra7) {
        this.extra7 = extra7;
        return this;
    }

    public String getExtra8() {
        return extra8;
    }

    public User setExtra8(String extra8) {
        this.extra8 = extra8;
        return this;
    }

    public String getExtra9() {
        return extra9;
    }

    public User setExtra9(String extra9) {
        this.extra9 = extra9;
        return this;
    }

    public String getExtra10() {
        return extra10;
    }

    public User setExtra10(String extra10) {
        this.extra10 = extra10;
        return this;
    }
}

package entity;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Table(name = "fbc_component")
public class Detail {

    @Column(name = "component_ID")
    @Id
    private Integer component_ID ;

    @Column(name = "dbType")
    private String dbType ;

    @Column(name = "dbVersion")
    private Integer dbVersion;

    @Column(name = "key_MasterName")
    private String key_MasterName;

    @Column(name = "key_TypeName")
    private String key_TypeName;

    @Column(name = "key_StateName")
    private String key_StateName ;

    @Column(name = "key_WorkspaceID")
    private Integer key_WorkspaceID;

    @Column(name = "rawMasterName")
    private String rawMasterName;

    @Column(name = "activeKey_MasterName")
    private String activeKey_MasterName;

    @Column(name = "activeKey_TypeName")
    private String activeKey_TypeName;

    @Column(name = "activeKey_StateName")
    private String activeKey_StateName;

    @Column(name = "activeKey_WorkspaceID")
    private Integer activeKey_WorkspaceID;

    @Column(name = "hasBaseline")
    private Byte hasBaseline;

    public Detail(Integer component_ID, String dbType, Integer dbVersion, String key_MasterName, String key_TypeName, String key_StateName, Integer key_WorkspaceID, String rawMasterName, String activeKey_MasterName, String activeKey_TypeName, String activeKey_StateName, Integer activeKey_WorkspaceID, Byte hasBaseline) {
        this.component_ID = component_ID;
        this.dbType = dbType;
        this.dbVersion = dbVersion;
        this.key_MasterName = key_MasterName;
        this.key_TypeName = key_TypeName;
        this.key_StateName = key_StateName;
        this.key_WorkspaceID = key_WorkspaceID;
        this.rawMasterName = rawMasterName;
        this.activeKey_MasterName = activeKey_MasterName;
        this.activeKey_TypeName = activeKey_TypeName;
        this.activeKey_StateName = activeKey_StateName;
        this.activeKey_WorkspaceID = activeKey_WorkspaceID;
        this.hasBaseline = hasBaseline;
    }

    public Integer getComponent_ID() {
        return component_ID;
    }

    public void setComponent_ID(Integer component_ID) {
        this.component_ID = component_ID;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public Integer getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(Integer dbVersion) {
        this.dbVersion = dbVersion;
    }

    public String getKey_MasterName() {
        return key_MasterName;
    }

    public void setKey_MasterName(String key_MasterName) {
        this.key_MasterName = key_MasterName;
    }

    public String getKey_TypeName() {
        return key_TypeName;
    }

    public void setKey_TypeName(String key_TypeName) {
        this.key_TypeName = key_TypeName;
    }

    public String getKey_StateName() {
        return key_StateName;
    }

    public void setKey_StateName(String key_StateName) {
        this.key_StateName = key_StateName;
    }

    public Integer getKey_WorkspaceID() {
        return key_WorkspaceID;
    }

    public void setKey_WorkspaceID(Integer key_WorkspaceID) {
        this.key_WorkspaceID = key_WorkspaceID;
    }

    public String getRawMasterName() {
        return rawMasterName;
    }

    public void setRawMasterName(String rawMasterName) {
        this.rawMasterName = rawMasterName;
    }

    public String getActiveKey_MasterName() {
        return activeKey_MasterName;
    }

    public void setActiveKey_MasterName(String activeKey_MasterName) {
        this.activeKey_MasterName = activeKey_MasterName;
    }

    public String getActiveKey_TypeName() {
        return activeKey_TypeName;
    }

    public void setActiveKey_TypeName(String activeKey_TypeName) {
        this.activeKey_TypeName = activeKey_TypeName;
    }

    public String getActiveKey_StateName() {
        return activeKey_StateName;
    }

    public void setActiveKey_StateName(String activeKey_StateName) {
        this.activeKey_StateName = activeKey_StateName;
    }

    public Integer getActiveKey_WorkspaceID() {
        return activeKey_WorkspaceID;
    }

    public void setActiveKey_WorkspaceID(Integer activeKey_WorkspaceID) {
        this.activeKey_WorkspaceID = activeKey_WorkspaceID;
    }

    public Byte getHasBaseline() {
        return hasBaseline;
    }

    public void setHasBaseline(Byte hasBaseline) {
        this.hasBaseline = hasBaseline;
    }


    @Override
    public String toString() {
        return "Details class {" +
                "component_ID=" + component_ID +
                ", dbType='" + dbType + '\'' +
                ", dbVersion=" + dbVersion +
                ", key_MasterName='" + key_MasterName + '\'' +
                ", key_TypeName='" + key_TypeName + '\'' +
                ", key_StateName='" + key_StateName + '\'' +
                ", key_WorkspaceID=" + key_WorkspaceID +
                ", rawMasterName='" + rawMasterName + '\'' +
                ", activeKey_MasterName='" + activeKey_MasterName + '\'' +
                ", activeKey_TypeName='" + activeKey_TypeName + '\'' +
                ", activeKey_StateName='" + activeKey_StateName + '\'' +
                ", activeKey_WorkspaceID=" + activeKey_WorkspaceID +
                ", hasBaseline=" + hasBaseline +
                '}';
    }
}

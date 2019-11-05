package entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity @Table(name = "fbc_usergroupassoc")
public class UserGroups implements Serializable{

    private static final Long serialVersionUID = -231312312L;
    public UserGroups () {}
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "userGroupAssoc_ID")
    private int userGroupAssoc_ID;
    @Column(name = "user_ID")
    private Long user_ID;
    @Column(name = "groupTypeName")
    private String groupTypeName;
    @Column(name = "groupUuid")
    private String groupUuid;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    
    
    public UserGroups( Long user_ID, String groupTypeName, String groupUuid) {
        this.user_ID = user_ID;
        this.groupTypeName = groupTypeName;
        this.groupUuid = groupUuid;
    }
    
    public int getUserGroupAssoc_ID() {
        return userGroupAssoc_ID;
    }
    public void setUserGroupAssoc_ID(int userGroupAssoc_ID) {
        this.userGroupAssoc_ID = userGroupAssoc_ID;
    }
    public Long getUser_ID() {
        return user_ID;
    }
    public void setUser_ID(Long user_ID) {
        this.user_ID = user_ID;
    }
    public String getGroupTypeName() {
        return groupTypeName;
    }
    public void setGroupTypeName(String groupTypeName) {
        this.groupTypeName = groupTypeName;
    }
    public String getGroupUuid() {
        return groupUuid;
    }
    public void setGroupUuid(String groupUuid) {
        this.groupUuid = groupUuid;
    }
}

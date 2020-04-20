package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "fbc_usergroupassoc")
public class UserGroups implements Serializable {

    private static final Long serialVersionUID = -231312312L;

    public UserGroups() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userGroupAssoc_ID")
    private int userGroupAssocId;
    @Column(name = "user_ID")
    private Long userId;
    @Column(name = "groupTypeName")
    private String groupTypeName;
    @Column(name = "groupUuid")
    private String groupUuid;

    @ManyToOne
    @JoinColumn(name = "user_ID", insertable = false, updatable = false)
    private User user;

    public UserGroups(Long userId, String groupTypeName, String groupUuid) {
        this.userId = userId;
        this.groupTypeName = groupTypeName;
        this.groupUuid = groupUuid;
    }

    public int getUserGroupAssocId() {
        return userGroupAssocId;
    }

    public void setUserGroupAssocId(int userGroupAssocId) {
        this.userGroupAssocId = userGroupAssocId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

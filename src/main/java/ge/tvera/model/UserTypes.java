package ge.tvera.model;

import javax.persistence.*;

@Entity
@Table(name = "user_types", schema = "tvera")
public class UserTypes {
    private Integer userTypeId;
    private String userTypeName;

    @Id
    @Column(name = "user_type_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    @Basic
    @Column(name = "user_type_name", nullable = false, length = 200)
    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTypes userTypes = (UserTypes) o;

        if (userTypeId != null ? !userTypeId.equals(userTypes.userTypeId) : userTypes.userTypeId != null) return false;
        if (userTypeName != null ? !userTypeName.equals(userTypes.userTypeName) : userTypes.userTypeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userTypeId != null ? userTypeId.hashCode() : 0;
        result = 31 * result + (userTypeName != null ? userTypeName.hashCode() : 0);
        return result;
    }
}

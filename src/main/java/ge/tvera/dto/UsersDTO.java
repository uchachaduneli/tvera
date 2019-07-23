package ge.tvera.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ge.tvera.misc.JsonDateTimeSerializeSupport;
import ge.tvera.model.Users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsersDTO {

    private Integer userId;
    private String userDesc;
    private String userName;
    private String userPassword;
    private UsersTypeDTO type;
    private Integer deleted;
    @JsonSerialize(using = JsonDateTimeSerializeSupport.class)
    private Date createDate;

    public static int ADMINISTRATOR = 1;
    public static int OPERATOR = 2;
    public static int AUDITOR = 3;

    public static UsersDTO parse(Users record) {
        if (record != null) {
            UsersDTO dto = new UsersDTO();
            dto.setUserId(record.getUserId());
            dto.setUserDesc(record.getUserDesc());
            dto.setUserName(record.getUserName());
            dto.setUserPassword(record.getUserPassword());
            dto.setType(UsersTypeDTO.parse(record.getType()));
            dto.setDeleted(record.getDeleted());
            dto.setCreateDate(record.getCreateDate());
            return dto;
        } else return null;
    }

    public static List<UsersDTO> parseToList(List<Users> records) {
        ArrayList<UsersDTO> list = new ArrayList<UsersDTO>();
        for (Users record : records) {
            list.add(UsersDTO.parse(record));
        }
        return list;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public UsersTypeDTO getType() {
        return type;
    }

    public void setType(UsersTypeDTO type) {
        this.type = type;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}

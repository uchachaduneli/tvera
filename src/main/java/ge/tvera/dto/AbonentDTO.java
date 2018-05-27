package ge.tvera.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ge.tvera.misc.JsonDateTimeSerializeSupport;
import ge.tvera.model.Abonent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AbonentDTO {
    private Integer id;
    private String name;
    private String lastname;
    @JsonSerialize(using = JsonDateTimeSerializeSupport.class)
    private Date createDate;
    private Double bill;
    private StreetDTO street;
    private Integer streetId;

    public static AbonentDTO parse(Abonent record) {
        if (record != null) {
            AbonentDTO dto = new AbonentDTO();
            dto.setId(record.getId());
            dto.setName(record.getName());
            dto.setBill(record.getBill());
            dto.setLastname(record.getLastname());
            dto.setCreateDate(record.getCreateDate());
            dto.setStreet(StreetDTO.parse(record.getStreet()));
            dto.setStreetId(record.getStreet().getId());
            return dto;
        } else return null;
    }

    public static List<AbonentDTO> parseToList(List<Abonent> records) {
        ArrayList<AbonentDTO> list = new ArrayList<AbonentDTO>();
        for (Abonent record : records) {
            list.add(AbonentDTO.parse(record));
        }
        return list;
    }

    public Integer getStreetId() {
        return streetId;
    }

    public void setStreetId(Integer streetId) {
        this.streetId = streetId;
    }

    public StreetDTO getStreet() {
        return street;
    }

    public void setStreet(StreetDTO street) {
        this.street = street;
    }

    public Double getBill() {
        return bill;
    }

    public void setBill(Double bill) {
        this.bill = bill;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

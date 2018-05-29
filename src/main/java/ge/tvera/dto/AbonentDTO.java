package ge.tvera.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ge.tvera.misc.JsonDateSerializeSupport;
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
    private Integer incasatorId;
    private Integer districtId;
    private Double balance;
    private String abonentNumber;
    private String personalNumber;
    private String deviceNumber;
    private String comment;
    @JsonSerialize(using = JsonDateSerializeSupport.class)
    private Date billDate;
    private Date billDateFrom;
    private Date billDateTo;
    private StatusDTO status;
    private Integer statusId;
    private List<Integer> packages;

    public static AbonentDTO parse(Abonent record) {
        if (record != null) {
            AbonentDTO dto = new AbonentDTO();
            dto.setId(record.getId());
            dto.setName(record.getName());
            dto.setBill(record.getBill());
            dto.setBalance(record.getBalance());
            dto.setLastname(record.getLastname());
            dto.setCreateDate(record.getCreateDate());
            dto.setStreet(StreetDTO.parse(record.getStreet()));
            dto.setStreetId(record.getStreet().getId());
            dto.setAbonentNumber(record.getAbonentNumber());
            dto.setDeviceNumber(record.getDeviceNumber());
            dto.setPersonalNumber(record.getPersonalNumber());
            dto.setComment(record.getComment());
            dto.setBillDate(record.getBillDate());
            dto.setStatus(StatusDTO.parse(record.getStatus()));
            dto.setStatusId(record.getStatus().getId());
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

    public List<Integer> getPackages() {
        return packages;
    }

    public void setPackages(List<Integer> packages) {
        this.packages = packages;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

    public Date getBillDateFrom() {
        return billDateFrom;
    }

    public void setBillDateFrom(Date billDateFrom) {
        this.billDateFrom = billDateFrom;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Integer getIncasatorId() {
        return incasatorId;
    }

    public void setIncasatorId(Integer incasatorId) {
        this.incasatorId = incasatorId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getAbonentNumber() {
        return abonentNumber;
    }

    public void setAbonentNumber(String abonentNumber) {
        this.abonentNumber = abonentNumber;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
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

    public Date getBillDateTo() {
        return billDateTo;
    }

    public void setBillDateTo(Date billDateTo) {
        this.billDateTo = billDateTo;
    }
}

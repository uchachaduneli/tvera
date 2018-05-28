package ge.tvera.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ge.tvera.misc.JsonDateTimeSerializeSupport;
import ge.tvera.model.Payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentDTO {
    private Integer id;
    private Double amount;
    private AbonentDTO abonent;
    private Integer abonentId;
    @JsonSerialize(using = JsonDateTimeSerializeSupport.class)
    private Date createDate;
    private Date createDateFrom;
    private Date createDateTo;
    private String checkNumber;
    private String abonentNumber;
    private String name;
    private String lastname;
    private String personalNumber;
    private String deviceNumber;

    public static PaymentDTO parse(Payment record) {
        if (record != null) {
            PaymentDTO dto = new PaymentDTO();
            dto.setId(record.getId());
            dto.setAmount(record.getAmount());
            dto.setAbonent(AbonentDTO.parse(record.getAbonent()));
            dto.setAbonentNumber(dto.getAbonent().getAbonentNumber());
            dto.setPersonalNumber(dto.getAbonent().getPersonalNumber());
            dto.setAbonentId(record.getAbonent().getId());
            dto.setCreateDate(record.getCreateDate());
            dto.setCheckNumber(record.getCheckNumber());
            return dto;
        } else return null;
    }

    public static List<PaymentDTO> parseToList(List<Payment> records) {
        ArrayList<PaymentDTO> list = new ArrayList<PaymentDTO>();
        for (Payment record : records) {
            list.add(PaymentDTO.parse(record));
        }
        return list;
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

    public Date getCreateDateFrom() {
        return createDateFrom;
    }

    public void setCreateDateFrom(Date createDateFrom) {
        this.createDateFrom = createDateFrom;
    }

    public Date getCreateDateTo() {
        return createDateTo;
    }

    public void setCreateDateTo(Date createDateTo) {
        this.createDateTo = createDateTo;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public AbonentDTO getAbonent() {
        return abonent;
    }

    public void setAbonent(AbonentDTO abonent) {
        this.abonent = abonent;
    }

    public Integer getAbonentId() {
        return abonentId;
    }

    public void setAbonentId(Integer abonentId) {
        this.abonentId = abonentId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

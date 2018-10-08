package ge.tvera.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ge.tvera.misc.JsonDateSerializeSupport;
import ge.tvera.misc.JsonDateTimeSerializeSupport;
import ge.tvera.model.Payment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentDTO {

  static SimpleDateFormat dtfrmt = new SimpleDateFormat("MM-yyyy");

  private Integer id;
  private Double amount;
  private AbonentDTO abonent;
  private Integer abonentId;
  @JsonSerialize(using = JsonDateTimeSerializeSupport.class)
  private Date createDate;
  @JsonSerialize(using = JsonDateSerializeSupport.class)
  private Date payDate;
  private String strOperDate;
  private Date operationDate;
  private Date operationDateTo;
  private Date createDateFrom;
  private Date createDateTo;
  private String checkNumber;
  private String abonentNumber;
  private String name;
  private String lastname;
  private String personalNumber;
  private String deviceNumber;
  private String streetNumber;
  private String roomNumber;
  private UsersDTO user;
  private Integer userId;
  private Integer districtId;
  private Integer streetId;
  private Integer incasatorId;
  private Integer packageTypeId;
  private Integer bankPayment;
  private Integer isCredit;
  private double avans;
  private double daval;

  public static PaymentDTO parse(Payment record) {
    if (record != null) {
      PaymentDTO dto = new PaymentDTO();
      dto.setId(record.getId());
      dto.setAmount(record.getAmount());
      dto.setAbonent(AbonentDTO.parse(record.getAbonent()));
      dto.setAbonentNumber(dto.getAbonent().getId() + "");
      dto.setPersonalNumber(dto.getAbonent().getPersonalNumber());
      dto.setAbonentId(record.getAbonent().getId());
      dto.setCreateDate(record.getCreateDate());
      dto.setCheckNumber(record.getCheckNumber());
      dto.setUser(UsersDTO.parse(record.getUser()));
      dto.setPayDate(record.getPayDate());
      dto.setBankPayment(record.getBankPayment());
      dto.setAvans(record.getAvans());
      dto.setDaval(record.getDaval());
      dto.setOperationDate(record.getOperationDate());
      if (dto.getOperationDate() != null) {
        dto.setStrOperDate(dtfrmt.format(record.getOperationDate()));
      }
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

  public static SimpleDateFormat getDtfrmt() {
    return dtfrmt;
  }

  public static void setDtfrmt(SimpleDateFormat dtfrmt) {
    PaymentDTO.dtfrmt = dtfrmt;
  }

  public Integer getPackageTypeId() {
    return packageTypeId;
  }

  public void setPackageTypeId(Integer packageTypeId) {
    this.packageTypeId = packageTypeId;
  }

  public String getStreetNumber() {
    return streetNumber;
  }

  public void setStreetNumber(String streetNumber) {
    this.streetNumber = streetNumber;
  }

  public String getRoomNumber() {
    return roomNumber;
  }

  public void setRoomNumber(String roomNumber) {
    this.roomNumber = roomNumber;
  }

  public Integer getStreetId() {
    return streetId;
  }

  public void setStreetId(Integer streetId) {
    this.streetId = streetId;
  }

  public String getStrOperDate() {
    return strOperDate;
  }

  public void setStrOperDate(String strOperDate) {
    this.strOperDate = strOperDate;
  }

  public Date getOperationDateTo() {
    return operationDateTo;
  }

  public void setOperationDateTo(Date operationDateTo) {
    this.operationDateTo = operationDateTo;
  }

  public Date getOperationDate() {
    return operationDate;
  }

  public void setOperationDate(Date operationDate) {
    this.operationDate = operationDate;
  }

  public Integer getIsCredit() {
    return isCredit;
  }

  public void setIsCredit(Integer isCredit) {
    this.isCredit = isCredit;
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

  public Date getPayDate() {
    return payDate;
  }

  public void setPayDate(Date payDate) {
    this.payDate = payDate;
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

  public String getAbonentNumber() {
    return abonentNumber;
  }

  public void setAbonentNumber(String abonentNumber) {
    this.abonentNumber = abonentNumber;
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

  public UsersDTO getUser() {
    return user;
  }

  public void setUser(UsersDTO user) {
    this.user = user;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getDistrictId() {
    return districtId;
  }

  public void setDistrictId(Integer districtId) {
    this.districtId = districtId;
  }

  public Integer getIncasatorId() {
    return incasatorId;
  }

  public void setIncasatorId(Integer incasatorId) {
    this.incasatorId = incasatorId;
  }

  public Integer getBankPayment() {
    return bankPayment;
  }

  public void setBankPayment(Integer bankPayment) {
    this.bankPayment = bankPayment;
  }

  public double getAvans() {
    return avans;
  }

  public void setAvans(double avans) {
    this.avans = avans;
  }

  public double getDaval() {
    return daval;
  }

  public void setDaval(double daval) {
    this.daval = daval;
  }
}

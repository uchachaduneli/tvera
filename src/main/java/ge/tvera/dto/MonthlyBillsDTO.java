package ge.tvera.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ge.tvera.misc.JsonDateSerializeSupport;
import ge.tvera.misc.JsonDateTimeSerializeSupport;
import ge.tvera.model.MonthlyBills;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MonthlyBillsDTO {

  static SimpleDateFormat dtfrmt = new SimpleDateFormat("MM-yyyy");

  private Integer id;
  private AbonentDTO abonent;
  private Double amount;
  @JsonSerialize(using = JsonDateTimeSerializeSupport.class)
  private Timestamp createDate;
  @JsonSerialize(using = JsonDateSerializeSupport.class)
  private Date operDate;
  private Date operDateTo;
  private String strOperDate;

  public static MonthlyBillsDTO parse(MonthlyBills record) {
    MonthlyBillsDTO dto = new MonthlyBillsDTO();
    dto.setId(record.getId());
    dto.setAbonent(AbonentDTO.parse(record.getAbonent()));
    dto.setAmount(record.getAmount());
    dto.setOperDate(record.getOperDate());
    if (record.getOperDate() != null) {
      dto.setStrOperDate(dtfrmt.format(record.getOperDate()));
    }
    return dto;
  }

  public static List<MonthlyBillsDTO> parseToList(List<MonthlyBills> records) {
    ArrayList<MonthlyBillsDTO> list = new ArrayList<MonthlyBillsDTO>();
    for (MonthlyBills record : records) {
      list.add(MonthlyBillsDTO.parse(record));
    }
    return list;
  }

  public String getStrOperDate() {
    return strOperDate;
  }

  public void setStrOperDate(String strOperDate) {
    this.strOperDate = strOperDate;
  }

  public Date getOperDateTo() {
    return operDateTo;
  }

  public void setOperDateTo(Date operDateTo) {
    this.operDateTo = operDateTo;
  }

  public Date getOperDate() {
    return operDate;
  }

  public void setOperDate(Date operDate) {
    this.operDate = operDate;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public AbonentDTO getAbonent() {
    return abonent;
  }

  public void setAbonent(AbonentDTO abonent) {
    this.abonent = abonent;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Timestamp getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Timestamp createDate) {
    this.createDate = createDate;
  }
}

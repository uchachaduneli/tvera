package ge.tvera.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ge.tvera.misc.JsonDateTimeSerializeSupport;
import ge.tvera.model.MonthlyBills;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MonthlyBillsDTO {

  private Integer id;
  private AbonentDTO abonent;
  private Double amount;
  @JsonSerialize(using = JsonDateTimeSerializeSupport.class)
  private Timestamp createDate;

  public static MonthlyBillsDTO parse(MonthlyBills record) {
    MonthlyBillsDTO dto = new MonthlyBillsDTO();
    dto.setId(record.getId());
    dto.setAbonent(AbonentDTO.parse(record.getAbonent()));
    return dto;
  }

  public static List<MonthlyBillsDTO> parseToList(List<MonthlyBills> records) {
    ArrayList<MonthlyBillsDTO> list = new ArrayList<MonthlyBillsDTO>();
    for (MonthlyBills record : records) {
      list.add(MonthlyBillsDTO.parse(record));
    }
    return list;
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

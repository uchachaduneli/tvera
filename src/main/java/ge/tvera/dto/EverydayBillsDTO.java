package ge.tvera.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ge.tvera.misc.JsonDateTimeSerializeSupport;
import ge.tvera.model.EverydayBills;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EverydayBillsDTO {

  private Integer id;
  private AbonentDTO abonent;
  private Double amount;
  @JsonSerialize(using = JsonDateTimeSerializeSupport.class)
  private Timestamp createDate;

  public static EverydayBillsDTO parse(EverydayBills record) {
    EverydayBillsDTO dto = new EverydayBillsDTO();
    dto.setId(record.getId());
    dto.setAbonent(AbonentDTO.parse(record.getAbonent()));
    return dto;
  }

  public static List<EverydayBillsDTO> parseToList(List<EverydayBills> records) {
    ArrayList<EverydayBillsDTO> list = new ArrayList<EverydayBillsDTO>();
    for (EverydayBills record : records) {
      list.add(EverydayBillsDTO.parse(record));
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

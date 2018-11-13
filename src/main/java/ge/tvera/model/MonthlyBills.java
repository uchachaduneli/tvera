package ge.tvera.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "monthly_bills")
public class MonthlyBills {
  private Integer id;
  private Abonent abonent;
  private Double amount;
  private Timestamp createDate;
  private Date operDate;
  private Integer actionUser;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @JoinColumn(name = "abonent_id")
  @OneToOne
  public Abonent getAbonent() {
    return abonent;
  }

  public void setAbonent(Abonent abonent) {
    this.abonent = abonent;
  }

  @Basic
  @Column(name = "amount")
  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  @Basic
  @Column(name = "create_date", updatable = false, insertable = false)
  public Timestamp getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Timestamp createDate) {
    this.createDate = createDate;
  }

  @Basic
  @Column(name = "oper_date")
  public Date getOperDate() {
    return operDate;
  }

  public void setOperDate(Date operDate) {
    this.operDate = operDate;
  }

  @Basic
  @Column(name = "action_user")
  public Integer getActionUser() {
    return actionUser;
  }

  public void setActionUser(Integer actionUser) {
    this.actionUser = actionUser;
  }

}

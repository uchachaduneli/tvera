package ge.tvera.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
public class Payment {
  private Integer id;
  private Abonent abonent;
  private Double amount;
  private Timestamp createDate;
  private String checkNumber;
  private Users user;
  private Date payDate;
  private Integer isCredit;
  private Integer bankPayment;

  @Id
  @Column(name = "id", nullable = false)
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
  @Column(name = "amount", nullable = false, precision = 0)
  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  @Basic
  @Column(name = "create_date", nullable = false, updatable = false, insertable = false)
  public Timestamp getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Timestamp createDate) {
    this.createDate = createDate;
  }

  @Basic
  @Column(name = "check_number", length = 50)
  public String getCheckNumber() {
    return checkNumber;
  }

  public void setCheckNumber(String checkNumber) {
    this.checkNumber = checkNumber;
  }

  @JoinColumn(name = "user_id")
  @OneToOne
  public Users getUser() {
    return user;
  }

  public void setUser(Users user) {
    this.user = user;
  }

  @Basic
  @Column(name = "pay_date")
  public Date getPayDate() {
    return payDate;
  }

  public void setPayDate(Date payDate) {
    this.payDate = payDate;
  }

  @Basic
  @Column(name = "is_credit")
  public Integer getIsCredit() {
    return isCredit;
  }

  public void setIsCredit(Integer isCredit) {
    this.isCredit = isCredit;
  }

  @Basic
  @Column(name = "bank_payment")
  public Integer getBankPayment() {
    return bankPayment;
  }

  public void setBankPayment(Integer bankPayment) {
    this.bankPayment = bankPayment;
  }

}

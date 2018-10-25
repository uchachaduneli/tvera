package ge.tvera.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "abonent_packages")
public class AbonentPackages {
  private Integer id;
  private Abonent abonent;
  private Package packages;
  private Timestamp createDate;
  private Double juridicalPrice;
  private Double phisicalPrice;
  private Users user;
  private Integer pointsNumber;
  private Integer deleted = 1;

  public AbonentPackages(Abonent abonent, Package packages, Double juridicalPrice, Double phisicalPrice, Users user, Integer pointsNumber) {
    this.abonent = abonent;
    this.packages = packages;
    this.juridicalPrice = juridicalPrice;
    this.phisicalPrice = phisicalPrice;
    this.user = user;
    this.pointsNumber = pointsNumber;
  }

  public AbonentPackages() {
  }

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

  @JoinColumn(name = "package_id")
  @OneToOne
  public Package getPackages() {
    return packages;
  }

  public void setPackages(Package packages) {
    this.packages = packages;
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
  @Column(name = "juridical_price", nullable = true, precision = 0)
  public Double getJuridicalPrice() {
    return juridicalPrice;
  }

  public void setJuridicalPrice(Double juridicalPrice) {
    this.juridicalPrice = juridicalPrice;
  }

  @Basic
  @Column(name = "phisical_price", nullable = true, precision = 0)
  public Double getPhisicalPrice() {
    return phisicalPrice;
  }

  public void setPhisicalPrice(Double phisicalPrice) {
    this.phisicalPrice = phisicalPrice;
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
  @Column(name = "points_number", nullable = true)
  public Integer getPointsNumber() {
    return pointsNumber;
  }

  public void setPointsNumber(Integer pointsNumber) {
    this.pointsNumber = pointsNumber;
  }

  @Basic
  @Column(name = "deleted")
  public Integer getDeleted() {
    return deleted;
  }

  public void setDeleted(Integer deleted) {
    this.deleted = deleted;
  }

}

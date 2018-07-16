package ge.tvera.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
public class Abonent {
    private Integer id;
    private String name;
    private String lastname;
    private Timestamp createDate;
    private Double bill;
    private Street street;
    private Double balance;
    private String personalNumber;
    private String deviceNumber;
    private String comment;
    private Date billDate;
    private Status status;
    private Integer juridicalOrPhisical;
    private Integer servicePointsNumber;
    private PackageType packageType;
    private District district;
    private String streetNumber;
    private String floor;
    private String roomNumber;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "lastname", nullable = false, length = 50)
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
    @Column(name = "bill", precision = 0)
    public Double getBill() {
        return bill;
    }

    public void setBill(Double bill) {
        this.bill = bill;
    }

    @JoinColumn(name = "street_id")
    @OneToOne
    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    @Basic
    @Column(name = "balance", precision = 0)
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Basic
    @Column(name = "personal_number", nullable = false, length = 50)
    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    @Basic
    @Column(name = "device_number", nullable = true, length = 50)
    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    @Basic
    @Column(name = "comment", nullable = true, length = -1)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "bill_date")
    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    @JoinColumn(name = "status_id")
    @OneToOne
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Basic
    @Column(name = "juridical_or_phisical", nullable = false)
    public Integer getJuridicalOrPhisical() {
        return juridicalOrPhisical;
    }

    public void setJuridicalOrPhisical(Integer juridicalOrPhisical) {
        this.juridicalOrPhisical = juridicalOrPhisical;
    }

    @Basic
    @Column(name = "service_points_number", nullable = true)
    public Integer getServicePointsNumber() {
        return servicePointsNumber;
    }

    public void setServicePointsNumber(Integer servicePointsNumber) {
        this.servicePointsNumber = servicePointsNumber;
    }

    @JoinColumn(name = "package_type_id", nullable = true)
    @OneToOne
    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    @JoinColumn(name = "district_id")
    @OneToOne
    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @Basic
    @Column(name = "street_number")
    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    @Basic
    @Column(name = "floor")
    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @Basic
    @Column(name = "room_number")
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}

package ge.tvera.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ge.tvera.misc.JsonDateSerializeSupport;
import ge.tvera.misc.JsonDateTimeSerializeSupport;
import ge.tvera.model.Abonent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AbonentDTO {
    public static Integer JURIDICAL = 2;
    public static Integer PHISICAL = 1;
    static int daysCount = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
    public Integer forTickets;
    public String phone;
    private Integer id;
    private String name;
    private String lastname;
    @JsonSerialize(using = JsonDateTimeSerializeSupport.class)
    private Date createDate;
    private Double bill;
    private StreetDTO street;
    private Integer streetId;
    private Integer incasatorId;
    private Double balance;
    private String personalNumber;
    private String deviceNumber;
    private String comment;
    @JsonSerialize(using = JsonDateSerializeSupport.class)
    private Date billDate;
    @JsonSerialize(using = JsonDateSerializeSupport.class)
    private Date billDateFrom;
    @JsonSerialize(using = JsonDateSerializeSupport.class)
    private Date billDateTo;
    private StatusDTO status;
    private Integer statusId;
    private Integer juridicalOrPhisical;
    private Integer servicePointsNumber;
    private PackageTypeDTO packageType;
    private Integer packageTypeId;
    private DistrictDTO district;
    private Integer districtId;
    private Integer hasBill; // -1 დავალიანების მქონე აბონენტები; 0 ყველა
    private String streetNumber;
    private String floor;
    private String roomNumber;
    private Double installationBill;
    private Double restoreBill;
    private Double dailyBill;
    private Double collectedBill;
    private Double avans;

    public static AbonentDTO parse(Abonent record) {
        if (record != null) {
            AbonentDTO dto = new AbonentDTO();
            if (record.getBillDate() != null && record.getBill() != null) {
                dto.setDailyBill(record.getBill() / daysCount);
//                dto.setCollectedBill((TimeUnit.DAYS.convert(new Date().getTime() - record.getBillDate().getTime(), TimeUnit.MILLISECONDS)) * dto.getDailyBill());
            }
            dto.setCollectedBill(record.getCollectedBill());
            dto.setId(record.getId());
            dto.setName(record.getName());
            dto.setBill(record.getBill());
            dto.setInstallationBill(record.getInstallationBill());
            dto.setRestoreBill(record.getRestoreBill());
            dto.setBalance(record.getBalance());
            dto.setLastname(record.getLastname());
            dto.setCreateDate(record.getCreateDate());
            dto.setStreet(StreetDTO.parse(record.getStreet()));
            dto.setStreetId(record.getStreet().getId());
            dto.setDistrict(DistrictDTO.parse(record.getDistrict()));
            dto.setDistrictId(record.getDistrict().getId());
            dto.setDeviceNumber(record.getDeviceNumber());
            dto.setPersonalNumber(record.getPersonalNumber());
            dto.setComment(record.getComment());
            dto.setBillDate(record.getBillDate());
            dto.setStatus(StatusDTO.parse(record.getStatus()));
            dto.setStatusId(record.getStatus().getId());
            dto.setJuridicalOrPhisical(record.getJuridicalOrPhisical());
            dto.setServicePointsNumber(record.getServicePointsNumber());
            if (record.getPackageType() != null) {
                dto.setPackageType(PackageTypeDTO.parse(record.getPackageType()));
                dto.setPackageTypeId(record.getPackageType().getId());
            }
            dto.setStreetNumber(record.getStreetNumber());
            dto.setRoomNumber(record.getRoomNumber());
            dto.setFloor(record.getFloor());
            dto.setPhone(record.getPhone());
            dto.setAvans(record.getAvans());
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

    public Double getAvans() {
        return avans;
    }

    public void setAvans(Double avans) {
        this.avans = avans;
    }

    public static Integer getJURIDICAL() {
        return JURIDICAL;
    }

    public static void setJURIDICAL(Integer JURIDICAL) {
        AbonentDTO.JURIDICAL = JURIDICAL;
    }

    public static Integer getPHISICAL() {
        return PHISICAL;
    }

    public static void setPHISICAL(Integer PHISICAL) {
        AbonentDTO.PHISICAL = PHISICAL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getDailyBill() {
        return dailyBill;
    }

    public void setDailyBill(Double dailyBill) {
        this.dailyBill = dailyBill;
    }

    public Double getCollectedBill() {
        return collectedBill;
    }

    public void setCollectedBill(Double collectedBill) {
        this.collectedBill = collectedBill;
    }

    public Integer getForTickets() {
        return forTickets;
    }

    public void setForTickets(Integer forTickets) {
        this.forTickets = forTickets;
    }

    public Double getInstallationBill() {
        return installationBill;
    }

    public void setInstallationBill(Double installationBill) {
        this.installationBill = installationBill;
    }

    public Double getRestoreBill() {
        return restoreBill;
    }

    public void setRestoreBill(Double restoreBill) {
        this.restoreBill = restoreBill;
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

    public Double getBill() {
        return bill;
    }

    public void setBill(Double bill) {
        this.bill = bill;
    }

    public StreetDTO getStreet() {
        return street;
    }

    public void setStreet(StreetDTO street) {
        this.street = street;
    }

    public Integer getStreetId() {
        return streetId;
    }

    public void setStreetId(Integer streetId) {
        this.streetId = streetId;
    }

    public Integer getIncasatorId() {
        return incasatorId;
    }

    public void setIncasatorId(Integer incasatorId) {
        this.incasatorId = incasatorId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
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

    public Date getBillDateFrom() {
        return billDateFrom;
    }

    public void setBillDateFrom(Date billDateFrom) {
        this.billDateFrom = billDateFrom;
    }

    public Date getBillDateTo() {
        return billDateTo;
    }

    public void setBillDateTo(Date billDateTo) {
        this.billDateTo = billDateTo;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getJuridicalOrPhisical() {
        return juridicalOrPhisical;
    }

    public void setJuridicalOrPhisical(Integer juridicalOrPhisical) {
        this.juridicalOrPhisical = juridicalOrPhisical;
    }

    public Integer getServicePointsNumber() {
        return servicePointsNumber;
    }

    public void setServicePointsNumber(Integer servicePointsNumber) {
        this.servicePointsNumber = servicePointsNumber;
    }

    public PackageTypeDTO getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageTypeDTO packageType) {
        this.packageType = packageType;
    }

    public Integer getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(Integer packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public DistrictDTO getDistrict() {
        return district;
    }

    public void setDistrict(DistrictDTO district) {
        this.district = district;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getHasBill() {
        return hasBill;
    }

    public void setHasBill(Integer hasBill) {
        this.hasBill = hasBill;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}

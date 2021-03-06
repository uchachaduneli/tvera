package ge.tvera.dto;

import java.util.ArrayList;
import java.util.List;

public class PackageDTO {

    private Integer id;
    private String name;
    private Double personalPrice;
    private Double juridicalPrice;
    private Integer scheduler;
    private PackageGroupDTO group;
    private PackageTypeDTO type;
    private Integer groupId;
    private Integer typeId;
    private Integer externalPointCount;

    public static final int DISTRIBUTION = 1;
    public static final int INSTALLATION = 3;
    public static final int RESTORATION = 4;
    public static final int FINE = 5;
    public static final int DISTRIBUTION_ON_EXTRA_POINT = 6;
    public static final int EXTRA_POINT_INSTALLATION = 7;
    public static final int PORTIREBA = 8;


    public static int SCHEDULED = 1;
    public static int NOTSCHEDULED = 2;

    public static PackageDTO parse(ge.tvera.model.Package record) {
        if (record != null) {
            PackageDTO dto = new PackageDTO();
            dto.setId(record.getId());
            dto.setName(record.getName());
            dto.setPersonalPrice(record.getPersonalPrice());
            dto.setJuridicalPrice(record.getJuridicalPrice());
            dto.setType(PackageTypeDTO.parse(record.getType()));
            dto.setGroup(PackageGroupDTO.parse(record.getGroup()));
            dto.setTypeId(record.getType().getId());
            dto.setGroupId(record.getGroup().getId());
            dto.setScheduler(record.getScheduler());
            return dto;
        } else return null;
    }

    public static List<PackageDTO> parseToList(List<ge.tvera.model.Package> records) {
        ArrayList<PackageDTO> list = new ArrayList<PackageDTO>();
        for (ge.tvera.model.Package record : records) {
            list.add(PackageDTO.parse(record));
        }
        return list;
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

    public Double getPersonalPrice() {
        return personalPrice;
    }

    public void setPersonalPrice(Double personalPrice) {
        this.personalPrice = personalPrice;
    }

    public Double getJuridicalPrice() {
        return juridicalPrice;
    }

    public void setJuridicalPrice(Double juridicalPrice) {
        this.juridicalPrice = juridicalPrice;
    }

    public Integer getScheduler() {
        return scheduler;
    }

    public void setScheduler(Integer scheduler) {
        this.scheduler = scheduler;
    }

    public PackageGroupDTO getGroup() {
        return group;
    }

    public void setGroup(PackageGroupDTO group) {
        this.group = group;
    }

    public PackageTypeDTO getType() {
        return type;
    }

    public void setType(PackageTypeDTO type) {
        this.type = type;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getExternalPointCount() {
        return externalPointCount;
    }

    public void setExternalPointCount(Integer externalPointCount) {
        this.externalPointCount = externalPointCount;
    }

    public static int getDISTRIBUTION() {
        return DISTRIBUTION;
    }

    public static int getINSTALLATION() {
        return INSTALLATION;
    }

    public static int getRESTORATION() {
        return RESTORATION;
    }

    public static int getFINE() {
        return FINE;
    }

    public static int getDistributionOnExtraPoint() {
        return DISTRIBUTION_ON_EXTRA_POINT;
    }

    public static int getExtraPointInstallation() {
        return EXTRA_POINT_INSTALLATION;
    }

    public static int getPORTIREBA() {
        return PORTIREBA;
    }

    public static int getSCHEDULED() {
        return SCHEDULED;
    }

    public static void setSCHEDULED(int SCHEDULED) {
        PackageDTO.SCHEDULED = SCHEDULED;
    }

    public static int getNOTSCHEDULED() {
        return NOTSCHEDULED;
    }

    public static void setNOTSCHEDULED(int NOTSCHEDULED) {
        PackageDTO.NOTSCHEDULED = NOTSCHEDULED;
    }
}

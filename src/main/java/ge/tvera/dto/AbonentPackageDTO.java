package ge.tvera.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ge.tvera.misc.JsonDateTimeSerializeSupport;
import ge.tvera.model.AbonentPackages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AbonentPackageDTO {
    private Integer id;
    private Integer abonentId;
    private AbonentDTO abonent;
    private PackageDTO packages;
    private Integer packageId;
    private Integer pointsCount;
    @JsonSerialize(using = JsonDateTimeSerializeSupport.class)
    private Date createDate;

    public static AbonentPackageDTO parse(AbonentPackages record) {
        if (record != null) {
            AbonentPackageDTO dto = new AbonentPackageDTO();
            dto.setId(record.getId());
            dto.setPointsCount(record.getPointsCount());
            dto.setCreateDate(record.getCreateDate());
            dto.setAbonent(AbonentDTO.parse(record.getAbonent()));
            dto.setPackages(PackageDTO.parse(record.getPackages()));
            return dto;
        } else return null;
    }

    public static List<AbonentPackageDTO> parseToList(List<AbonentPackages> records) {
        ArrayList<AbonentPackageDTO> list = new ArrayList<AbonentPackageDTO>();
        for (AbonentPackages record : records) {
            list.add(AbonentPackageDTO.parse(record));
        }
        return list;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAbonentId() {
        return abonentId;
    }

    public void setAbonentId(Integer abonentId) {
        this.abonentId = abonentId;
    }

    public AbonentDTO getAbonent() {
        return abonent;
    }

    public void setAbonent(AbonentDTO abonent) {
        this.abonent = abonent;
    }

    public PackageDTO getPackages() {
        return packages;
    }

    public void setPackages(PackageDTO packages) {
        this.packages = packages;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getPointsCount() {
        return pointsCount;
    }

    public void setPointsCount(Integer pointsCount) {
        this.pointsCount = pointsCount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

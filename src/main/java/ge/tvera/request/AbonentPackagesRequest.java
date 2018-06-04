package ge.tvera.request;

import ge.tvera.dto.PackageDTO;

import java.util.List;

public class AbonentPackagesRequest {
    private List<PackageDTO> abonentPackages;
    private Integer packageTypeId;
    private Integer servicePointsNumber;
    private Integer abonendId;
    private Integer UserId;//author of package Actions

    public List<PackageDTO> getAbonentPackages() {
        return abonentPackages;
    }

    public void setAbonentPackages(List<PackageDTO> abonentPackages) {
        this.abonentPackages = abonentPackages;
    }

    public Integer getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(Integer packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public Integer getServicePointsNumber() {
        return servicePointsNumber;
    }

    public void setServicePointsNumber(Integer servicePointsNumber) {
        this.servicePointsNumber = servicePointsNumber;
    }

    public Integer getAbonendId() {
        return abonendId;
    }

    public void setAbonendId(Integer abonendId) {
        this.abonendId = abonendId;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }
}

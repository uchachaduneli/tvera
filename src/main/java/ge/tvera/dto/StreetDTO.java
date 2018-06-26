package ge.tvera.dto;

import ge.tvera.model.Street;

import java.util.ArrayList;
import java.util.List;

public class StreetDTO {
    private Integer id;
    private String name;
    private DistrictDTO district;
    private Integer districtId;
    private Integer incasatorId;


    public static StreetDTO parse(Street record) {
        if (record != null) {
            StreetDTO dto = new StreetDTO();
            dto.setId(record.getId());
            dto.setName(record.getName());
            dto.setDistrict(DistrictDTO.parse(record.getDistrict()));
            dto.setDistrictId(record.getDistrict() != null ? record.getDistrict().getId() : null);
            return dto;
        } else return null;
    }

    public static List<StreetDTO> parseToList(List<Street> records) {
        ArrayList<StreetDTO> list = new ArrayList<StreetDTO>();
        for (Street record : records) {
            list.add(StreetDTO.parse(record));
        }
        return list;
    }

    public Integer getIncasatorId() {
        return incasatorId;
    }

    public void setIncasatorId(Integer incasatorId) {
        this.incasatorId = incasatorId;
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
}

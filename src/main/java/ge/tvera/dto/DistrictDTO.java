package ge.tvera.dto;

import ge.tvera.model.District;

import java.util.ArrayList;
import java.util.List;

public class DistrictDTO {

    private Integer id;
    private String name;

    public static DistrictDTO parse(District record) {
        if (record != null) {
            DistrictDTO dto = new DistrictDTO();
            dto.setId(record.getId());
            dto.setName(record.getName());
            return dto;
        } else return null;
    }

    public static List<DistrictDTO> parseToList(List<District> records) {
        ArrayList<DistrictDTO> list = new ArrayList<DistrictDTO>();
        for (District record : records) {
            list.add(DistrictDTO.parse(record));
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
}

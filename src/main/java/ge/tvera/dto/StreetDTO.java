package ge.tvera.dto;

import ge.tvera.model.Street;

import java.util.ArrayList;
import java.util.List;

public class StreetDTO {
    private Integer id;
    private String name;


    public static StreetDTO parse(Street record) {
        if (record != null) {
            StreetDTO dto = new StreetDTO();
            dto.setId(record.getId());
            dto.setName(record.getName());
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

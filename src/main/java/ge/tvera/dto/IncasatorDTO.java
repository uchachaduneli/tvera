package ge.tvera.dto;

import ge.tvera.model.Incasator;

import java.util.ArrayList;
import java.util.List;

public class IncasatorDTO {

    private Integer id;
    private String name;
    private String lastname;

    public static IncasatorDTO parse(Incasator record) {
        if (record != null) {
            IncasatorDTO dto = new IncasatorDTO();
            dto.setId(record.getId());
            dto.setName(record.getName());
            dto.setLastname(record.getLastname());
            return dto;
        } else return null;
    }

    public static List<IncasatorDTO> parseToList(List<Incasator> records) {
        ArrayList<IncasatorDTO> list = new ArrayList<IncasatorDTO>();
        for (Incasator record : records) {
            list.add(IncasatorDTO.parse(record));
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}

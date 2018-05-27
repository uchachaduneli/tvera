package ge.tvera.dto;

import ge.tvera.model.PackageType;

import java.util.ArrayList;
import java.util.List;

public class PackageTypeDTO {

    private Integer id;
    private String name;

    public static PackageTypeDTO parse(PackageType record) {
        if (record != null) {
            PackageTypeDTO dto = new PackageTypeDTO();
            dto.setId(record.getId());
            dto.setName(record.getName());
            return dto;
        } else return null;
    }

    public static List<PackageTypeDTO> parseToList(List<PackageType> records) {
        ArrayList<PackageTypeDTO> list = new ArrayList<PackageTypeDTO>();
        for (PackageType record : records) {
            list.add(PackageTypeDTO.parse(record));
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

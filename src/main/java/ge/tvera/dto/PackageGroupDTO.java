package ge.tvera.dto;

import ge.tvera.model.PackageGroup;

import java.util.ArrayList;
import java.util.List;

public class PackageGroupDTO {

    private Integer id;
    private String name;

    public static PackageGroupDTO parse(PackageGroup record) {
        if (record != null) {
            PackageGroupDTO dto = new PackageGroupDTO();
            dto.setId(record.getId());
            dto.setName(record.getName());
            return dto;
        } else return null;
    }

    public static List<PackageGroupDTO> parseToList(List<PackageGroup> records) {
        ArrayList<PackageGroupDTO> list = new ArrayList<PackageGroupDTO>();
        for (PackageGroup record : records) {
            list.add(PackageGroupDTO.parse(record));
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

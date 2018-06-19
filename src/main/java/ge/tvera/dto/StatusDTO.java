package ge.tvera.dto;

import ge.tvera.model.Status;

import java.util.ArrayList;
import java.util.List;

public class StatusDTO {

    private Integer id;
    private String name;

    public static int STATUS_ACTIVE = 1;
    public static int STATUS_DISABLED = 2;


    public static StatusDTO parse(Status record) {
        StatusDTO dto = new StatusDTO();
        dto.setId(record.getId());
        dto.setName(record.getName());
        return dto;
    }


    public static List<StatusDTO> parseToList(List<Status> records) {
        ArrayList<StatusDTO> list = new ArrayList<StatusDTO>();
        for (Status record : records) {
            list.add(StatusDTO.parse(record));
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

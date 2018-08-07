package ge.tvera.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ge.tvera.misc.JsonDateSerializeSupport;
import ge.tvera.model.StatusHistory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatusHistoryDTO {

    private AbonentDTO abonent;
    private Integer abonentId;
    private StatusDTO status;
    private Integer statusId;
    @JsonSerialize(using = JsonDateSerializeSupport.class)
    private Date createDate;
    @JsonSerialize(using = JsonDateSerializeSupport.class)
    private Date disableDate;


    public static StatusHistoryDTO parse(StatusHistory record) {
        StatusHistoryDTO dto = new StatusHistoryDTO();
        dto.setAbonent(AbonentDTO.parse(record.getAbonent()));
        dto.setStatus(StatusDTO.parse(record.getStatus()));
        dto.setAbonentId(record.getAbonent().getId());
        dto.setStatusId(record.getStatus().getId());
        dto.setCreateDate(record.getCreateDate());
        dto.setDisableDate(record.getDisableDate());
        return dto;
    }


    public static List<StatusHistoryDTO> parseToList(List<StatusHistory> records) {
        ArrayList<StatusHistoryDTO> list = new ArrayList<StatusHistoryDTO>();
        for (StatusHistory record : records) {
            list.add(StatusHistoryDTO.parse(record));
        }
        return list;
    }

    public Date getDisableDate() {
        return disableDate;
    }

    public void setDisableDate(Date disableDate) {
        this.disableDate = disableDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public AbonentDTO getAbonent() {
        return abonent;
    }

    public void setAbonent(AbonentDTO abonent) {
        this.abonent = abonent;
    }

    public Integer getAbonentId() {
        return abonentId;
    }

    public void setAbonentId(Integer abonentId) {
        this.abonentId = abonentId;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
}

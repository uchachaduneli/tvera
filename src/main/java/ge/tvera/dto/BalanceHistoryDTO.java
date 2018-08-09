package ge.tvera.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ge.tvera.misc.JsonDateSerializeSupport;
import ge.tvera.model.BalanceHistory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BalanceHistoryDTO {
    private Integer id;
    private Integer abonentId;
    private AbonentDTO abonent;
    @JsonSerialize(using = JsonDateSerializeSupport.class)
    private Date createDate;
    private Double balance;
    private Double installationBalance;
    private Double restoreBalance;

    public static BalanceHistoryDTO parse(BalanceHistory record) {
        if (record != null) {
            BalanceHistoryDTO dto = new BalanceHistoryDTO();
            dto.setId(record.getId());
            dto.setCreateDate(record.getCreateDate());
            dto.setAbonent(AbonentDTO.parse(record.getAbonent()));
            dto.setAbonentId(record.getAbonent().getId());
            dto.setBalance(record.getBalance());
            dto.setInstallationBalance(record.getInstallationBalance());
            dto.setRestoreBalance(record.getRestoreBalance());
            return dto;
        } else return null;
    }

    public static List<BalanceHistoryDTO> parseToList(List<BalanceHistory> records) {
        ArrayList<BalanceHistoryDTO> list = new ArrayList<BalanceHistoryDTO>();
        for (BalanceHistory record : records) {
            list.add(BalanceHistoryDTO.parse(record));
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getInstallationBalance() {
        return installationBalance;
    }

    public void setInstallationBalance(Double installationBalance) {
        this.installationBalance = installationBalance;
    }

    public Double getRestoreBalance() {
        return restoreBalance;
    }

    public void setRestoreBalance(Double restoreBalance) {
        this.restoreBalance = restoreBalance;
    }
}

package ge.tvera.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "balance_history", schema = "tvera")
public class BalanceHistory {
    private Integer id;
    private Abonent abonent;
    private Double balance;
    private Double installationBalance;
    private Double restoreBalance;
    private Timestamp createDate;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JoinColumn(name = "abonent_id", nullable = true)
    @OneToOne
    public Abonent getAbonent() {
        return abonent;
    }

    public void setAbonent(Abonent abonent) {
        this.abonent = abonent;
    }

    @Basic
    @Column(name = "balance", nullable = true, precision = 0)
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Basic
    @Column(name = "installation_balance", nullable = true, precision = 0)
    public Double getInstallationBalance() {
        return installationBalance;
    }

    public void setInstallationBalance(Double installationBalance) {
        this.installationBalance = installationBalance;
    }

    @Basic
    @Column(name = "restore_balance", nullable = true, precision = 0)
    public Double getRestoreBalance() {
        return restoreBalance;
    }

    public void setRestoreBalance(Double restoreBalance) {
        this.restoreBalance = restoreBalance;
    }

    @Basic
    @Column(name = "create_date", updatable = false, insertable = false)
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}

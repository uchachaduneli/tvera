package ge.tvera.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "abonent_packages", schema = "tvera")
public class AbonentPackages {
    private Integer id;
    private Abonent abonent;
    private Package packages;
    private Integer pointsCount;
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

    @JoinColumn(name = "abonent_id")
    @OneToOne
    public Abonent getAbonent() {
        return abonent;
    }

    public void setAbonent(Abonent abonent) {
        this.abonent = abonent;
    }

    @JoinColumn(name = "package_id")
    @OneToOne
    public Package getPackages() {
        return packages;
    }

    public void setPackages(Package packages) {
        this.packages = packages;
    }

    @Basic
    @Column(name = "points_count", nullable = false)
    public Integer getPointsCount() {
        return pointsCount;
    }

    public void setPointsCount(Integer pointsCount) {
        this.pointsCount = pointsCount;
    }

    @Basic
    @Column(name = "create_date", nullable = false, updatable = false, insertable = false)
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}

package ge.tvera.model;

import javax.persistence.*;

@Entity
public class Package {
    private Integer id;
    private String name;
    private Double personalPrice;
    private Double juridicalPrice;
    private Integer scheduler;
    private PackageGroup group;
    private PackageType type;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 300)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JoinColumn(name = "group_id")
    @OneToOne
    public PackageGroup getGroup() {
        return group;
    }

    public void setGroup(PackageGroup group) {
        this.group = group;
    }

    @JoinColumn(name = "type_id")
    @OneToOne
    public PackageType getType() {
        return type;
    }

    public void setType(PackageType type) {
        this.type = type;
    }

    @Basic
    @Column(name = "personal_price", nullable = true, precision = 0)
    public Double getPersonalPrice() {
        return personalPrice;
    }

    public void setPersonalPrice(Double personalPrice) {
        this.personalPrice = personalPrice;
    }

    @Basic
    @Column(name = "juridical_price", nullable = true, precision = 0)
    public Double getJuridicalPrice() {
        return juridicalPrice;
    }

    public void setJuridicalPrice(Double juridicalPrice) {
        this.juridicalPrice = juridicalPrice;
    }

    @Basic
    @Column(name = "scheduler", nullable = false)
    public Integer getScheduler() {
        return scheduler;
    }

    public void setScheduler(Integer scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Package aPackage = (Package) o;

        if (id != null ? !id.equals(aPackage.id) : aPackage.id != null) return false;
        if (name != null ? !name.equals(aPackage.name) : aPackage.name != null) return false;
        if (personalPrice != null ? !personalPrice.equals(aPackage.personalPrice) : aPackage.personalPrice != null)
            return false;
        if (juridicalPrice != null ? !juridicalPrice.equals(aPackage.juridicalPrice) : aPackage.juridicalPrice != null)
            return false;
        if (scheduler != null ? !scheduler.equals(aPackage.scheduler) : aPackage.scheduler != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (personalPrice != null ? personalPrice.hashCode() : 0);
        result = 31 * result + (juridicalPrice != null ? juridicalPrice.hashCode() : 0);
        result = 31 * result + (scheduler != null ? scheduler.hashCode() : 0);
        return result;
    }
}

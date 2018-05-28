package ge.tvera.model;

import javax.persistence.*;

@Entity
public class Street {
    private Integer id;
    private String name;
    private Incasator incasator;
    private District district;

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
    @Column(name = "name", nullable = false, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JoinColumn(name = "incasator_id")
    @OneToOne
    public Incasator getIncasator() {
        return incasator;
    }

    public void setIncasator(Incasator incasator) {
        this.incasator = incasator;
    }

    @JoinColumn(name = "district_id")
    @OneToOne
    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }
}

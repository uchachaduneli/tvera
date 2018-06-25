package ge.tvera.model;

import javax.persistence.*;

@Entity
public class District {
    private Integer id;
    private String name;
    private Incasator incasator;
    private Street street;

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
    @Column(name = "name", nullable = false, length = 500)
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

    @JoinColumn(name = "street_id")
    @OneToOne
    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }
}

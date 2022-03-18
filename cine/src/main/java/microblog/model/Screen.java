package microblog.model;

import java.util.Date;
import java.util.List;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;

import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import microblog.model.Projection;


@Entity
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 256)
    private String name;

    @Column(nullable = false)
    private Integer total_seats;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id")
    private List<Projection> projections;

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

    public Integer getTotal_seats() {
        return total_seats;
    }
    public void setTotal_seats(Integer total_seats) {
        this.total_seats = total_seats;
    }

    public List <Projection> getProjections() {
        return projections;
    }
    public void setProjections(List<Projection> projections) {
        this.projections = projections;
    }
}
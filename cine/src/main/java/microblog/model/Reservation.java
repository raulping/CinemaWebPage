package microblog.model;

import java.util.Date;
import java.sql.Time;

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
import microblog.model.Movie;
import microblog.model.User;
import microblog.model.Projection;
import javax.persistence.OneToMany;



@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(optional = false)
    private Projection projection;
    
    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private int seats;

    @Column(nullable = false)
    private Date date;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Projection getProjection() {
        return projection;
    }
    public void setProjection(Projection projection) {
        this.projection = projection;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public int getSeats() {
        return seats;
    }
    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
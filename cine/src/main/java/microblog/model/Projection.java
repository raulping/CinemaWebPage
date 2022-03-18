package microblog.model;

import java.util.Date;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

import microblog.model.Movie;
import microblog.model.Screen;
import microblog.model.Reservation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Entity
public class Projection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(optional = false)
    private Movie movie;
    
    @ManyToOne(optional = false)
    private Screen screen;

    @Column(nullable = false)
    private Integer available_seats;

    @Column(nullable = false)
    private Date date;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private List<Reservation> reservations;


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Screen getScreen() {
        return screen;
    }
    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public Integer getAvailable_seats() {
        return available_seats;
    }
    public void setAvailable_seats(Integer available_seats) {
        this.available_seats = available_seats;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public List <Reservation> getReservations() {
        return reservations;
    }
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
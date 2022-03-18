package microblog.model;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import microblog.model.Reservation;



public interface ProjectionRepository extends CrudRepository<Projection, Integer> {
    @Query("SELECT SUM(r.seats) FROM Reservation r WHERE r.projection=?1")
    Integer sumReservedSeats(Projection projection);
    
}
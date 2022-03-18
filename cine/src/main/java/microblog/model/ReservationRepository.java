package microblog.model;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;


public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    Reservation findById(String reservationId);
    List<Reservation> findAll();

    @Transactional
    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.projection=?1")
    void deleteReservations(Projection projection); 
}
package microblog.model;
import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface MovieRepository extends CrudRepository<Movie, Integer> {
    Movie findByTitle(String title);
    List<Movie> findAll();

}
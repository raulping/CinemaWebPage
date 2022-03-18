package microblog.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ScreenRepository extends CrudRepository<Screen, Integer> {
    Screen findByName(String name);
    List<Screen> findAll();
}
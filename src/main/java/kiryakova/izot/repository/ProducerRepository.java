package kiryakova.izot.repository;

import kiryakova.izot.domain.entities.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, String> {
    Optional<Producer> findByName(String name);

    @Query(value = "SELECT * FROM producers ORDER BY producer_name ASC"
            , nativeQuery = true)
    List<Producer> findAllProducers();
}

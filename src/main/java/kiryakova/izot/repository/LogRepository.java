package kiryakova.izot.repository;

import kiryakova.izot.domain.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {

    @Query(value = "SELECT * FROM logs ORDER BY date_time DESC"
            , nativeQuery = true)
    List<Log> findAllLogsByDateTimeDesc();
}

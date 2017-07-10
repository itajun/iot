package au.ivj.sandbox.iot.repositories;

import au.ivj.sandbox.iot.entities.Reading;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Basic reading repo.
 */
@CrossOrigin
public interface ReadingRepository extends CrudRepository<Reading, Long>, PagingAndSortingRepository<Reading, Long> {
    @Query(value = "select r from Reading r join r.device d join r.sensor s where d.name = ?1 and " +
            "(?2 is null or s.name = ?2)",
            countQuery = "select count(r) from Reading r join r.device d join r.sensor s where d.name = ?1 and " +
                    "(?2 is null or s.name = ?2)")
    Page<Reading> findSummaryForDevice(@Param("device") String deviceName,
                                              @Param("sensor") String sensorName,
                                              Pageable pagination);
}

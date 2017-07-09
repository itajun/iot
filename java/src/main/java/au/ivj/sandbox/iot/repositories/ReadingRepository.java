package au.ivj.sandbox.iot.repositories;

import au.ivj.sandbox.iot.entities.Device;
import au.ivj.sandbox.iot.entities.Reading;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Basic reading repo.
 */
public interface ReadingRepository extends CrudRepository<Reading, Long>, PagingAndSortingRepository<Reading, Long> {
}

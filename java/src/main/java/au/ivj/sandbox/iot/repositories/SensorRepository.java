package au.ivj.sandbox.iot.repositories;

import au.ivj.sandbox.iot.entities.Sensor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

/**
 * Basic sensor repo.
 */
@RolesAllowed("ROLE_USER")
public interface SensorRepository extends CrudRepository<Sensor, Long> {
    @Query(value = "select s from Device d inner join d.sensors s where d.name = ?2 and s.name = ?1")
    Optional<Sensor> findByNameAndDeviceName(@Param("name") String name, @Param("deviceName") String deviceName);
}

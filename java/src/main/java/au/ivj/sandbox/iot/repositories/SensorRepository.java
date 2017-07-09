package au.ivj.sandbox.iot.repositories;

import au.ivj.sandbox.iot.entities.Device;
import au.ivj.sandbox.iot.entities.Sensor;
import org.springframework.data.repository.CrudRepository;

import javax.annotation.security.RolesAllowed;

/**
 * Basic sensor repo.
 */
@RolesAllowed("ROLE_USER")
public interface SensorRepository extends CrudRepository<Sensor, Long> {
}

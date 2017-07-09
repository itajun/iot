package au.ivj.sandbox.iot.repositories;

import au.ivj.sandbox.iot.entities.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.annotation.security.RolesAllowed;

/**
 * Basic device repo.
 */
@RolesAllowed("ROLE_USER")
public interface DeviceRepository extends CrudRepository<Device, Long> {
    Device findByName(@Param("name") String name);
}

package au.ivj.sandbox.iot.repositories;

import au.ivj.sandbox.iot.entities.DeviceLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.annotation.security.RolesAllowed;

/**
 * Basic device log repo.
 */
@RolesAllowed("ROLE_USER")
@CrossOrigin
public interface DeviceLogRepository extends CrudRepository<DeviceLog, Long>, PagingAndSortingRepository<DeviceLog,
        Long>
{
}

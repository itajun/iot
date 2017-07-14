package au.ivj.sandbox.iot.repositories;

import au.ivj.sandbox.iot.entities.DeviceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
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
    @Query(value = "select dl from DeviceLog dl join dl.device d where d.name = ?1",
            countQuery = "select count(dl) from DeviceLog dl join dl.device d where d.name = ?1")
    Page<DeviceLog> findSummaryForDevice(@Param("device") String deviceName,
                                         Pageable pagination);
}

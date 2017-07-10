package au.ivj.sandbox.iot.entities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

/**
 * Returns the basic data of the reading, without notes and concatenating the device and sensor names instead of
 * returning links.
 */
@Projection(name="readingSummary", types = {Reading.class})
public interface ReadingSummary
{
    Date getDateTime();
    Double getReading();
    @Value("#{target.device.name}.#{target.sensor.name}")
    String getSensorFQN();
}

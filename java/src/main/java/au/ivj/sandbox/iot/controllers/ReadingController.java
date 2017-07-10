package au.ivj.sandbox.iot.controllers;

import au.ivj.sandbox.iot.entities.Device;
import au.ivj.sandbox.iot.entities.Reading;
import au.ivj.sandbox.iot.entities.Sensor;
import au.ivj.sandbox.iot.repositories.DeviceRepository;
import au.ivj.sandbox.iot.repositories.ReadingRepository;
import au.ivj.sandbox.iot.repositories.SensorRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.security.RolesAllowed;
import java.util.Date;
import java.util.Optional;

/**
 * Exposes some methods with simplified signature to be called by simple devices like Arduino and ESP8266.
 */
@Controller
public class ReadingController
{
    private static final Logger LOGGER = Logger.getLogger(ReadingController.class);

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    ReadingRepository readingRepository;

    /**
     * Posts a new reading
     * @param deviceName Device name
     * @param sensorName Sensor name
     * @param value Value of the reading
     * @param when When the reading took place. If not provided, will use current date/time
     * @param note Optional note on the reading
     */
    @RolesAllowed("USER")
    @RequestMapping(path = "/postReading", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void postReading(@RequestParam(value="device") String deviceName,
                            @RequestParam(value="sensor") String sensorName,
                            @RequestParam(value="value") Double value,
                            @RequestParam(value="when", required = false) @DateTimeFormat(pattern="ddMMyyyyHHmmss")
                                        Date when,
                            @RequestParam(value="note", required = false) String note) {
        Optional<Device> foundDevice = deviceRepository.findByName(deviceName);
        Device device = foundDevice.orElseThrow(() ->
                new IllegalArgumentException(String.format("Couldn't find device [%s]", deviceName)));

        Optional<Sensor> foundSensor = sensorRepository.findByNameAndDeviceName(sensorName, deviceName);
        Sensor sensor = foundSensor.orElseThrow(() ->
                new IllegalArgumentException(String.format("Couldn't find sensor [%s.%s]", deviceName, sensorName)));

        Reading reading = new Reading();
        reading.setDateTime(when == null ? new Date() : when);
        reading.setDevice(device);
        reading.setSensor(sensor);
        reading.setReading(value);
        reading.setNote(note);
        readingRepository.save(reading);
    }
}

package au.ivj.sandbox.iot;

import au.ivj.sandbox.iot.entities.Device;
import au.ivj.sandbox.iot.entities.Reading;
import au.ivj.sandbox.iot.entities.Sensor;
import au.ivj.sandbox.iot.repositories.DeviceRepository;
import au.ivj.sandbox.iot.repositories.ReadingRepository;
import au.ivj.sandbox.iot.repositories.SensorRepository;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Hop;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {IOTApplication.class, IOTApplicationTests.IOTApplicationTestConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class IOTApplicationTests {

    SecureRandom secureRandom = new SecureRandom();

    @LocalServerPort
    int localServerPort;

    @Autowired
    EntityManager entityManager;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    TestRestTemplate testRestTemplateAuthenticated;

    @Autowired
	DeviceRepository deviceRepository;

	@Autowired
	SensorRepository sensorRepository;

	@Autowired
	ReadingRepository readingRepository;

    private Traverson baseTraverson() {
        return new Traverson(URI.create(String.format("http://localhost:%d/", localServerPort)), MediaTypes.HAL_JSON);
    }

    private Traverson traversonFor(URI uri) {
        return new Traverson(uri, MediaTypes.HAL_JSON);
    }

    @Test
    public void allTests() {
        deviceTestsJPA();
        sensorTestsJPA();
        readingTestsJPA();
        profileTestsRest();
        retrievingRestTest();
        postingRestTest();
    }

    public void deviceTestsJPA() {
        Device device = new Device();
        device.setName("test device");
        device.setLocation("test location");
        deviceRepository.save(device);

        Device byName = deviceRepository.findByName("test device");
        assertEquals(byName.getName(), "test device");
    }

    public void sensorTestsJPA() {
        Sensor sensor = new Sensor();
        sensor.setName("test sensor");
        sensor.setType(Sensor.SensorType.HUMIDITY);
        sensor.setUnity(Sensor.SensorMeasurementUnity.UNITY);
        Sensor saved = sensorRepository.save(sensor);
        Device device = deviceRepository.findByName("test device");
        assertEquals(device.getSensors().size(), 0);
        device.getSensors().add(saved);
        deviceRepository.save(device);
        Device byName = deviceRepository.findByName("test device");
        assertNotNull(byName);
        assertEquals(byName.getSensors().size(), 1);
        assertEquals(byName.getSensors().get(0).getName(), "test sensor");
    }

    public void readingTestsJPA() {
        Device device = deviceRepository.findByName("test device");
        Sensor sensor = device.getSensors().get(0);

        for (int i = 0; i < 10; i++) {
            Reading reading = new Reading();
            reading.setSensor(sensor);
            reading.setDateTime(Date.from(LocalDateTime.now().minusMinutes(secureRandom.nextInt(120)).atZone(ZoneId.systemDefault()).toInstant()));
            reading.setReading(secureRandom.nextDouble());
            reading.setNote("note");
            reading.setDevice(device);
            readingRepository.save(reading);
        }

        Page<Reading> allReadings = readingRepository.findAll(new PageRequest(1, 5, new Sort(Sort.Direction.DESC, "dateTime")));
        assertEquals(allReadings.getSize(), 5);
        assertEquals(allReadings.getTotalPages(), 2);
        assertEquals(device.getName(), allReadings.iterator().next().getDevice().getName());
        assertEquals(sensor.getName(), allReadings.iterator().next().getSensor().getName());
    }

    public void profileTestsRest() {
        ResponseEntity<Resource> profiles = baseTraverson().follow("profile").toEntity(Resource.class);
        assertNotNull(profiles.getBody().getLink("devices"));
        assertNotNull(profiles.getBody().getLink("sensors"));
        assertNotNull(profiles.getBody().getLink("readings"));
    }

    public void retrievingRestTest() {
        Resource<Device> deviceResource = baseTraverson()
                .follow("devices", "search")
                .follow(Hop.rel("findByName").withParameter("name", "test device"))
                .toObject(new ParameterizedTypeReference<Resource<Device>>() {});
        assertNotNull(deviceResource.getContent());
        assertEquals("test device", deviceResource.getContent().getName());

        Resources<Sensor> sensorResources = traversonFor(URI.create(deviceResource.getLink("sensors").getHref()))
                .follow()
                .toObject(new ParameterizedTypeReference<Resources<Sensor>>() {
                });
        assertNotNull(sensorResources);
        assertEquals("test sensor", sensorResources.getContent().iterator().next().getName());

        Resources<Reading> readings = baseTraverson()
                .follow("readings")
                .follow(Hop.rel("self").withParameter("page", 1).withParameter("size", 5))
                .toObject(new ParameterizedTypeReference<Resources<Reading>>() {
                });

        assertNotNull(readings.getLink("first"));
        assertNotNull(readings.getLink("prev"));
        assertNotNull(readings.getLink("self"));
        assertNotNull(readings.getLink("last"));
        assertNull(readings.getLink("next"));
        Reading randomReading = readings.getContent().iterator().next();
        assertNotNull(randomReading.getReading());
    }

    public void postingRestTest() {
        Device device = new Device();
        device.setName("test device rest");
        device.setLocation("location device rest");
        ResponseEntity<Void> createdDevice = testRestTemplateAuthenticated.postForEntity(
                URI.create(baseTraverson().follow("devices").asLink().getHref()),
                device,
                Void.class
        );
        assertEquals(HttpStatus.CREATED, createdDevice.getStatusCode());

        Sensor sensor = new Sensor();
        sensor.setName("test sensor rest");
        sensor.setUnity(Sensor.SensorMeasurementUnity.UNITY);
        sensor.setType(Sensor.SensorType.HUMIDITY);
        ResponseEntity<Void> createdSensor = testRestTemplateAuthenticated.postForEntity(
                URI.create(baseTraverson().follow("sensors").asLink().getHref()),
                sensor,
                Void.class
        );
        assertEquals(HttpStatus.CREATED, createdSensor.getStatusCode());

        /*
         * IMPORTANT
         * By default Spring Data Rest won't exporse "POST/PUT" for a sublist. However, we can always implement an endpoint
         * to handle a POST/PUT to devices/1/sensors in the controller. Not required here though, just a PATCH is enough.
         */
        String createdDevicePath = createdDevice.getHeaders().get("Location").get(0);
        String createdSensorPath = createdSensor.getHeaders().get("Location").get(0);
        ResponseEntity<Void> updatedDevice = testRestTemplateAuthenticated.exchange(
                RequestEntity.patch(URI.create(createdDevicePath))
                        .body(ImmutableMap.of("sensors", Collections.singletonList(createdSensorPath))),
                Void.class
        );
        assertEquals(HttpStatus.OK, updatedDevice.getStatusCode());

        Resources<Sensor> sensors = baseTraverson()
                .follow("devices", "search")
                .follow(Hop.rel("findByName").withParameter("name", "test device rest"))
                .follow("sensors")
                .toObject(new ParameterizedTypeReference<Resources<Sensor>>() {});
        assertNotNull(sensors);
        assertEquals("test sensor rest", sensors.getContent().iterator().next().getName());

        ResponseEntity<Void> createdReading = testRestTemplateAuthenticated.postForEntity(
                URI.create(baseTraverson().follow("readings").asLink().getHref()),
                new SampleClientSideReading(
                        new Date(),
                        2d,
                        URI.create(createdDevicePath),
                        URI.create(createdSensorPath),
                        "Note"
                ),
                Void.class
        );
        assertEquals(HttpStatus.CREATED, createdSensor.getStatusCode());

    }

    /**
     * This is how an entity used only on the client side would look like
     */
    public class SampleClientSideReading {
        Date dateTime;
        Double reading;
        URI device;
        URI sensor;
        String note;

        public SampleClientSideReading() {}

        public SampleClientSideReading(Date dateTime, Double reading, URI device, URI sensor, String note) {
            this.dateTime = dateTime;
            this.reading = reading;
            this.device = device;
            this.sensor = sensor;
            this.note = note;
        }

        public Date getDateTime() {
            return dateTime;
        }

        public void setDateTime(Date dateTime) {
            this.dateTime = dateTime;
        }

        public Double getReading() {
            return reading;
        }

        public void setReading(Double reading) {
            this.reading = reading;
        }

        public URI getDevice() {
            return device;
        }

        public void setDevice(URI device) {
            this.device = device;
        }

        public URI getSensor() {
            return sensor;
        }

        public void setSensor(URI sensor) {
            this.sensor = sensor;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    @Configuration
    public static class IOTApplicationTestConfiguration {
        @Bean
        public TestRestTemplate testRestTemplate() {
            return new TestRestTemplate();
        }

        @Bean
        public TestRestTemplate testRestTemplateAuthenticated(@Value("${security.user.name}") String name, @Value("${security.user.password}") String password) {
            return testRestTemplate().withBasicAuth(name, password);
        }
    }
}

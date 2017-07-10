package au.ivj.sandbox.iot.entities;


import javax.persistence.*;

/**
 * A sensor pertaining to a device.
 */
@Entity
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    String name;
    SensorType type;
    SensorMeasurementUnit unit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }

    public SensorMeasurementUnit getUnit() {
        return unit;
    }

    public void setUnit(SensorMeasurementUnit unit) {
        this.unit = unit;
    }

    /**
     * Short list of sensor types
     */
    public enum SensorType {
        TEMPERATURE,
        HUMIDITY,
        MOISTURE,
        LIGHT
    }

    /**
     * Unit of measurement
     */
    public enum SensorMeasurementUnit
    {
        UNIT,
        DEGREE,
        VOLTAGE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sensor)) return false;

        Sensor sensor = (Sensor) o;

        if (id != null ? !id.equals(sensor.id) : sensor.id != null) return false;
        if (name != null ? !name.equals(sensor.name) : sensor.name != null) return false;
        if (type != sensor.type) return false;
        return unit == sensor.unit;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        return result;
    }
}

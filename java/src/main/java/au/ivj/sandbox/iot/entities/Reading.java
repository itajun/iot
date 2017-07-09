package au.ivj.sandbox.iot.entities;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * A reading from the sensor.
 */
@Entity
public class Reading {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    Date dateTime;
    @Column(nullable = false)
    Double reading;
    @ManyToOne(optional = false)
    Device device;
    @ManyToOne(optional = false)
    Sensor sensor;
    String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reading)) return false;

        Reading reading1 = (Reading) o;

        if (id != null ? !id.equals(reading1.id) : reading1.id != null) return false;
        if (dateTime != null ? !dateTime.equals(reading1.dateTime) : reading1.dateTime != null) return false;
        if (reading != null ? !reading.equals(reading1.reading) : reading1.reading != null) return false;
        return note != null ? note.equals(reading1.note) : reading1.note == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (reading != null ? reading.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        return result;
    }
}

package au.ivj.sandbox.iot.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Any relevant log, like a command that was processed.
 */
@Entity
public class DeviceLog
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    Date dateTime;
    String groupName;
    @Column(nullable = false)
    String details;
    @ManyToOne
    Device device;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(Date dateTime)
    {
        this.dateTime = dateTime;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getDetails()
    {
        return details;
    }

    public void setDetails(String details)
    {
        this.details = details;
    }

    public Device getDevice()
    {
        return device;
    }

    public void setDevice(Device device)
    {
        this.device = device;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof DeviceLog))
        {
            return false;
        }

        DeviceLog deviceLog = (DeviceLog) o;

        if (id != null ? !id.equals(deviceLog.id) : deviceLog.id != null)
        {
            return false;
        }
        if (dateTime != null ? !dateTime.equals(deviceLog.dateTime) : deviceLog.dateTime != null)
        {
            return false;
        }
        if (groupName != null ? !groupName.equals(deviceLog.groupName) : deviceLog.groupName != null)
        {
            return false;
        }
        if (details != null ? !details.equals(deviceLog.details) : deviceLog.details != null)
        {
            return false;
        }
        return device != null ? device.equals(deviceLog.device) : deviceLog.device == null;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (device != null ? device.hashCode() : 0);
        return result;
    }
}

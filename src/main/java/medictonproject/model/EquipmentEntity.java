package medictonproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "equipment", schema = "swprojekt")
public class EquipmentEntity {
    private int equipmentId;
    private String buisnessName;
    private String serialNumber;
    private String riskClass;
    private String producer;
    private String servisContact;
    private Date startDate;
    @JsonIgnore
    private UserEntity owner;
    @JsonIgnore
    private List<ProtocolEntity> protocols;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    @OneToMany(mappedBy = "equipment" ,  cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    public List<ProtocolEntity> getProtocols(){
        return this.protocols;
    }

    public void setProtocols( List<ProtocolEntity> protocols){
        this.protocols= protocols;
    }

    @Id
    @Column(name = "equipment_id", nullable = false)
    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    @Basic
    @Column(name = "buisness_name", nullable = true, length = 100)
    public String getBuisnessName() {
        return buisnessName;
    }

    public void setBuisnessName(String buisnessName) {
        this.buisnessName = buisnessName;
    }

    @Basic
    @Column(name = "serial_number", nullable = true, length = 100)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Basic
    @Column(name = "risk_class", nullable = true, length = 100)
    public String getRiskClass() {
        return riskClass;
    }

    public void setRiskClass(String riskClass) {
        this.riskClass = riskClass;
    }

    @Basic
    @Column(name = "producer", nullable = true, length = 100)
    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    @Basic
    @Column(name = "servis_contact", nullable = true, length = 100)
    public String getServisContact() {
        return servisContact;
    }

    public void setServisContact(String servisContact) {
        this.servisContact = servisContact;
    }

    @Basic
    @Column(name = "start_date", nullable = true)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EquipmentEntity that = (EquipmentEntity) o;

        if (equipmentId != that.equipmentId) return false;
        if (buisnessName != null ? !buisnessName.equals(that.buisnessName) : that.buisnessName != null) return false;
        if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null) return false;
        if (riskClass != null ? !riskClass.equals(that.riskClass) : that.riskClass != null) return false;
        if (producer != null ? !producer.equals(that.producer) : that.producer != null) return false;
        if (servisContact != null ? !servisContact.equals(that.servisContact) : that.servisContact != null)
            return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = equipmentId;
        result = 31 * result + (buisnessName != null ? buisnessName.hashCode() : 0);
        result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
        result = 31 * result + (riskClass != null ? riskClass.hashCode() : 0);
        result = 31 * result + (producer != null ? producer.hashCode() : 0);
        result = 31 * result + (servisContact != null ? servisContact.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        return result;
    }
}

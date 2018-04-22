package medictonproject.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "protocol", schema = "swprojekt")
public class ProtocolEntity {
    private int protocolId;
    private String name;
    private Timestamp expires;
    private List<DocumentEntity> documents;
    private EquipmentEntity equipment;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    public EquipmentEntity getEquipment(){
        return this.equipment;
    }

    public void setEquipment( EquipmentEntity eq ){
        this.equipment = eq;
    }

    @OneToMany(mappedBy = "protocol" ,  cascade = {CascadeType.ALL})
    public List<DocumentEntity> getDocuments(){
        return documents;
    }

    public void setDocuments( List<DocumentEntity> documents){
        this.documents = documents;
    }


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "protocol_id", nullable = false)
    public int getDocumentId() {
        return protocolId;
    }

    public void setDocumentId(int protocolId) {
        this.protocolId= protocolId;
    }

    @Basic
    @Column(name = "protocol_name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Basic
    @Column(name = "protocol_expire", nullable = false)
    public Timestamp getExpires() {
        return expires;
    }

    public void setExpires(Timestamp expires) {
        this.expires= expires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProtocolEntity)) return false;

        ProtocolEntity that = (ProtocolEntity) o;

        if (protocolId != that.protocolId) return false;
        if (!getName().equals(that.getName())) return false;
        if (!expires.equals(that.expires)) return false;
        if (!getDocuments().equals(that.getDocuments())) return false;
        return getEquipment().equals(that.getEquipment());
    }

    @Override
    public int hashCode() {
        int result = protocolId;
        result = 31 * result + getName().hashCode();
        result = 31 * result + expires.hashCode();
        result = 31 * result + getDocuments().hashCode();
        result = 31 * result + getEquipment().hashCode();
        return result;
    }
}

package medictonproject.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "document", schema = "swprojekt")
public class DocumentEntity {
    private int documentId;
    private String name;
    private String path;
    private Timestamp created;
    private ProtocolEntity protocol;

    @ManyToOne(fetch=FetchType.LAZY , cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "protocol_id")
    public ProtocolEntity getProtocol(){
        return this.protocol;
    }

    public void setProtocol( ProtocolEntity protocol ){
        this.protocol= protocol;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "document_id", nullable = false)
    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "path", nullable = false, length = 100)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Basic
    @Column(name = "created", nullable = false)
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentEntity that = (DocumentEntity) o;

        if (documentId != that.documentId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = documentId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }
}

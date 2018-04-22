package medictonproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctorspecialization", schema = "swprojekt")
public class DoctorspecializationEntity {
    private String name;
    private int doctorSpecializationId;
    @JsonIgnore
    private List<UserEntity> users = new ArrayList<>();
    @JsonIgnore
    private List<AnnouncementEntity> annoucements = new ArrayList<>();
    
    @ManyToMany(mappedBy="specializations")
    public List<UserEntity> getUsers() {
        return this.users;
    }
    
    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    @ManyToMany(mappedBy="specializations")
    public List<AnnouncementEntity> getAnnoucements() {
        return this.annoucements;
    }
    
    public void setAnnoucements( List<AnnouncementEntity> list ) {
        this.annoucements = list;
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "doctor_specialization_id", nullable = false)
    public int getDoctorSpecializationId() {
        return doctorSpecializationId;
    }

    public void setDoctorSpecializationId(int doctorSpecializationId) {
        this.doctorSpecializationId = doctorSpecializationId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoctorspecializationEntity that = (DoctorspecializationEntity) o;

        if (doctorSpecializationId != that.doctorSpecializationId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + doctorSpecializationId;
        return result;
    }
}

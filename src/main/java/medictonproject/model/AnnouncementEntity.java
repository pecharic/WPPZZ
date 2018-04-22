package medictonproject.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "announcement", schema = "swprojekt")
public class AnnouncementEntity {
    private int announcementId;
    private String title;
    private String text;
    private Date date;
    private int isAnnouncement;
    private int isPublished;
    private List<DoctorspecializationEntity> specializations;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
      name="join_announcement_to_doctor_specialization",
      joinColumns=@JoinColumn(name="announcement_id", referencedColumnName="announcement_id"),
      inverseJoinColumns=@JoinColumn(name="doctor_specialization_id", referencedColumnName="doctor_specialization_id"))
    public List<DoctorspecializationEntity> getSpecializations() {
        return specializations;
    }
    
    public void setSpecializations( List<DoctorspecializationEntity> list ) {
        this.specializations = list;
    }
    
    @Id
    @Column(name = "announcement_id", nullable = false)
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    public int getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(int announcementId) {
        this.announcementId = announcementId;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 500)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "text", nullable = false, length = 5000)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(name = "date", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Basic
    @Column(name = "is_announcement", nullable = false)
    public int getIsAnnouncement() {
        return isAnnouncement;
    }

    public void setIsAnnouncement(int isAnnouncement) {
        this.isAnnouncement = isAnnouncement;
    }

    @Basic
    @Column(name = "is_published", nullable = false)
    public int getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(int isPublished) {
        this.isPublished = isPublished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnouncementEntity that = (AnnouncementEntity) o;

        if (announcementId != that.announcementId) return false;
        if (isAnnouncement != that.isAnnouncement) return false;
        if (isPublished != that.isPublished) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = announcementId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + isAnnouncement;
        result = 31 * result + isPublished;
        return result;
    }
}

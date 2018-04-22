package medictonproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user", schema = "swprojekt", uniqueConstraints = {
@UniqueConstraint(columnNames = "user_id") })
public class UserEntity implements Serializable{
    private int userId;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String ico;
    private String addressHouseNumber;
    private String addressStreet;
    private String addressCity;
    private String addressState;
    private String phoneNumber;
    private int isAdmin;
    @JsonIgnore
    private List<DoctorspecializationEntity> specializations = new ArrayList<>();
    @JsonIgnore
    private List<SurveyAnswerEntity> answers = new ArrayList<>();
    @JsonIgnore
    private List<EquipmentEntity> equipments = new ArrayList<>();
    @JsonIgnore
    private List<ConversationEntity> conversations = new ArrayList<>();
    @JsonIgnore
    private List<MessageEntity> messages = new ArrayList<>();

    @OneToMany(mappedBy = "messageOwner", fetch = FetchType.EAGER)
    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }

    @OneToMany(mappedBy="conversationOwner")
    public List<ConversationEntity> getConversations() {
        return conversations;
    }

    public void setConversations(List<ConversationEntity> conversations) {
        this.conversations = conversations;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name="join_doctor_specialization_to_user",
            joinColumns=@JoinColumn(name="user_id", referencedColumnName="user_id"),
            inverseJoinColumns=@JoinColumn(name="doctor_specialization_id", referencedColumnName="doctor_specialization_id"))
    public List<DoctorspecializationEntity> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<DoctorspecializationEntity> specializations) {
        this.specializations = specializations;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    public int getUserId() {
            return userId;
        }

    public void setUserId(int userId) {
            this.userId = userId;
        }

    @OneToMany(mappedBy = "owner")
    public List<EquipmentEntity> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<EquipmentEntity> equipments) {
        this.equipments = equipments;
    }

    @OneToMany(mappedBy = "user")
    public List<SurveyAnswerEntity> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SurveyAnswerEntity> answrers) {
        this.answers= answrers;
    }


    @Basic
    @Column(name = "first_name", nullable = false, length = 100)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name", nullable = false, length = 100)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "ico", nullable = false, length = 8)
    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    @Basic
    @Column(name = "address_house_number", nullable = false, length = 100)
    public String getAddressHouseNumber() {
        return addressHouseNumber;
    }

    public void setAddressHouseNumber(String addressHouseNumber) {
        this.addressHouseNumber = addressHouseNumber;
    }

    @Basic
    @Column(name = "address_street", nullable = false, length = 100)
    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    @Basic
    @Column(name = "address_city", nullable = false, length = 100)
    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    @Basic
    @Column(name = "address_state", nullable = false, length = 100)
    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    @Basic
    @Column(name = "phone_number", nullable = false, length = 50)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "is_admin", nullable = false)
    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (userId != that.userId) return false;
        if (isAdmin != that.isAdmin) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (addressHouseNumber != null ? !addressHouseNumber.equals(that.addressHouseNumber) : that.addressHouseNumber != null)
            return false;
        if (addressStreet != null ? !addressStreet.equals(that.addressStreet) : that.addressStreet != null)
            return false;
        if (addressCity != null ? !addressCity.equals(that.addressCity) : that.addressCity != null) return false;
        if (addressState != null ? !addressState.equals(that.addressState) : that.addressState != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (addressHouseNumber != null ? addressHouseNumber.hashCode() : 0);
        result = 31 * result + (addressStreet != null ? addressStreet.hashCode() : 0);
        result = 31 * result + (addressCity != null ? addressCity.hashCode() : 0);
        result = 31 * result + (addressState != null ? addressState.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + isAdmin;
        return result;
    }
}

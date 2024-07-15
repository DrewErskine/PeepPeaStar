package peep.pea.collection.beans;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Date;

import org.hibernate.mapping.List;

@Entity
@Table(name = "peeppea_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Should be a well-formed email address")
    private String email;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, max = 20, message = "Size must be between 3 and 20")
    private String name;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, max = 20, message = "Size must be between 6 and 20")
    private String password;

    private String bio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_registered", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date dateRegistered;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
}
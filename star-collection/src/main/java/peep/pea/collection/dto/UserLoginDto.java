package peep.pea.collection.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Email;

public class UserLoginDto {

    @NotEmpty(message = "Password cannot be empty")
    @Email
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package peep.pea.collection.dto;

import java.util.Date;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for user messages.
 */
public class UserMessageDto {

    @Size(min = 3, max = 333, message = "Message must be between 3 and 333 characters")
    private String message;

    // Default constructor
    public UserMessageDto() {
    }

    // Constructor with fields
    public UserMessageDto(String message) {
        this.message = message;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
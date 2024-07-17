package peep.pea.collection.dto;

import java.util.Date;

import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for user messages.
 */
public class UserMessageDto {

    private int userId;

    @Size(min = 3, max = 333)
    private String message;
    
    private Date timestamp;

    // Default constructor
    public UserMessageDto() {
    }

    // Constructor with fields
    public UserMessageDto(int userId, String message, Date timestamp) {
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    // ToString method
    @Override
    public String toString() {
        return "UserMessageDto{" +
               "userId=" + userId +
               ", message='" + message + '\'' +
               ", timestamp=" + timestamp +
               '}';
    }
}
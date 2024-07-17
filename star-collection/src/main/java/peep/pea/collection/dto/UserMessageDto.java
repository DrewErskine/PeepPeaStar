package peep.pea.collection.dto;

import java.util.Date;

/**
 * Data Transfer Object for user messages.
 */
public class UserMessageDto {

    private int userId;
    private String content;
    private Date timestamp;

    // Default constructor
    public UserMessageDto() {
    }

    // Constructor with fields
    public UserMessageDto(int userId, String content, Date timestamp) {
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
               ", content='" + content + '\'' +
               ", timestamp=" + timestamp +
               '}';
    }
}

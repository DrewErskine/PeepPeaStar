package peep.pea.collection.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserCommentDto {

    @NotNull(message = "Blog ID cannot be null")
    private Integer blogId;

    @Size(min = 3, max = 500, message = "Comment must be between 3 and 500 characters")
    private String comment;

    public UserCommentDto() {
    }

    public UserCommentDto(Integer blogId, String comment) {
        this.blogId = blogId;
        this.comment = comment;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
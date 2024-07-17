package peep.pea.collection.beans;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "peeppea_comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    @Column(name = "user_id")
    private Long user;

    @Column(nullable = false)
    private String content;

    @Column(name = "date_posted", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePosted = new Date();

    // Constructors
    public Comment() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Long getUserId() {
        return user;
    }

    public void setUserId(Long userId) {
        this.user = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }
}
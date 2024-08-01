package peep.pea.collection.dao;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import peep.pea.collection.beans.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByBlogId(Integer blogId);
    
    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.blog.id = :blogId")
    List<Comment> findByBlogIdWithUser(@Param("blogId") Integer blogId);
}
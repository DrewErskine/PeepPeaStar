package peep.pea.collection.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import peep.pea.collection.beans.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}

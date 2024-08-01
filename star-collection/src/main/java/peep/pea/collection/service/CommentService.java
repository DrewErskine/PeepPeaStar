package peep.pea.collection.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import peep.pea.collection.beans.Comment;
import peep.pea.collection.dao.CommentRepository;

public class CommentService {

        @Autowired
    private CommentRepository commentRepository;

    public void saveComment(Comment comment) {
        comment.setDatePosted(new Date());
        commentRepository.save(comment);
    }
    
}

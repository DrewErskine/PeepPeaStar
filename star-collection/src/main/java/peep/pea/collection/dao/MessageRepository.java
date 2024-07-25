package peep.pea.collection.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import peep.pea.collection.beans.Message;
import peep.pea.collection.beans.User;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {

    @Query("SELECT m FROM Message m WHERE m.user.id = :userId")
    List<Message> findByUserId(@Param("userId") int userId);
    void deleteAllByUser(User user);
    
}

package peep.pea.collection.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import peep.pea.collection.beans.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
    
}

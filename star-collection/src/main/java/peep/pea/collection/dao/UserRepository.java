package peep.pea.collection.dao;

import peep.pea.collection.beans.User;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    <S extends User> S save(S entity);
    User findByName(String name);
    Optional<User> findByEmail(String email);
}
package peep.pea.collection.dao;

import peep.pea.collection.beans.Blog;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends CrudRepository<Blog, Integer> {

    @Query("select p from Blog p where p.title like %:searchString%")
    public List<Blog> searchByTitle(@Param("searchString") String keyword);

    @Override
    Iterable<Blog> findAll();
}
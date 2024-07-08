package peep.pea.collection.dao;

import peep.pea.collection.beans.Blog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends CrudRepository<Blog, Integer> {

    @Query("select p from Blog p where p.name like %:searchString%")
    public List<Blog> searchByName(@Param("searchString") String keyword);
}
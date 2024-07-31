package peep.pea.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import peep.pea.collection.beans.Blog;
import peep.pea.collection.dao.BlogRepository;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public int incrementLikes(Integer blogId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new IllegalArgumentException("Invalid blog ID"));
        blog.setLikes(blog.getLikes() + 1);
        Blog savedBlog = blogRepository.save(blog);
        System.out.println("Updated blog likes: " + savedBlog.getLikes());
        return savedBlog.getLikes();
    }
}

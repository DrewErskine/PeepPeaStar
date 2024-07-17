package peep.pea.collection.controller;

import peep.pea.collection.beans.Blog;
import peep.pea.collection.beans.Comment;
import peep.pea.collection.dao.BlogRepository;
import peep.pea.collection.dao.CommentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.Executor;

@Controller
public class BlogController {

    private final Logger logger = LoggerFactory.getLogger(BlogController.class);

    private BlogRepository blogRepository;

    private CommentRepository commentRepository;

    private Executor asyncExecutor;

    public BlogController(BlogRepository blogRepository, CommentRepository commentRepository, Executor asyncExecutor) {
        this.blogRepository = blogRepository;
        this.commentRepository = commentRepository;
        this.asyncExecutor = asyncExecutor;
    }

    @GetMapping("/blog/{id}")
    public String getBlogDetails(@PathVariable("id") int id, Model model) {
        return blogRepository.findById(id)
            .map(blog -> {
                model.addAttribute("blog", blog);
                model.addAttribute("comment", new Comment());
                return "blog-detail";
            })
            .orElse("redirect:/blogs");
    }

    @PostMapping("/addPeepComment")
    public String addComment(@ModelAttribute Comment comment) {
        if (comment.getBlog() != null) {
            Blog blog = blogRepository.findById(comment.getBlog().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));
            comment.setBlog(blog);
        }
        commentRepository.save(comment);
        return "redirect:/blog-detail/" + comment.getBlog().getId();
    }

    @PostMapping("/search")
    public String search(@RequestParam("searchString") String keyword, Model model){

        List<Blog> blogs = blogRepository.searchByTitle(keyword);
        model.addAttribute("blogs", blogs);
        model.addAttribute("searchedFor", keyword);

        return "search-results";
    }

    @GetMapping("/getAllBlogs")
    public DeferredResult<String> getAllBlogs(Model model){
        DeferredResult<String> deferredResult = new DeferredResult<>();
        asyncExecutor.execute(()->{
            model.addAttribute("blogs", getBlogs());
            deferredResult.setResult("blog-list");
        });
        return deferredResult;
    }

    private Iterable<Blog> getBlogs(){

        logger.info("Getting all blogs, using spring executor thread");
        try{
            Thread.sleep(3000);
        }
        catch (InterruptedException exception){
            throw new RuntimeException();
        }
        return  blogRepository.findAll();
    }
}
package peep.pea.collection.controller;

import peep.pea.collection.beans.Blog;
import peep.pea.collection.dao.BlogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.Executor;

@Controller
public class BlogController {

    private final Logger logger = LoggerFactory.getLogger(BlogController.class);

    private BlogRepository blogRepository;

    private Executor asyncExecutor;

    public BlogController(BlogRepository blogRepository, Executor asyncExecutor) {
        this.asyncExecutor = asyncExecutor;
        this.blogRepository = blogRepository;
    }

    @PostMapping("/search")
    public String search(@RequestParam("searchString") String keyword, Model model){

        List<Blog> blogs = blogRepository.searchByName(keyword);
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
            Thread.sleep(6000);
        }
        catch (InterruptedException exception){
            throw new RuntimeException();
        }
        return  null;
    }
}
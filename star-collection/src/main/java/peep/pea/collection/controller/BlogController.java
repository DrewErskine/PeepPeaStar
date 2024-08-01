package peep.pea.collection.controller;

import peep.pea.collection.beans.Blog;
import peep.pea.collection.beans.Comment;
import peep.pea.collection.beans.User;
import peep.pea.collection.dao.BlogRepository;
import peep.pea.collection.dao.CommentRepository;
import peep.pea.collection.dao.UserRepository;
import peep.pea.collection.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;

@Controller
public class BlogController extends CommonController {

    @Autowired
    private BlogService blogService;

    private final Logger logger = LoggerFactory.getLogger(BlogController.class);

    private BlogRepository blogRepository;
    private CommentRepository commentRepository;
    private Executor asyncExecutor;

    public BlogController(UserRepository userRepository, BlogRepository blogRepository, CommentRepository commentRepository, Executor asyncExecutor) {
        super(userRepository);
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

    @PostMapping("/likeBlog/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> likeBlog(@PathVariable Integer id) {
        int likes = blogService.incrementLikes(id);
        Map<String, Object> response = new HashMap<>();
        response.put("likes", likes);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addPeepComment")
    public String addComment(@ModelAttribute("comment") @Valid Comment comment, BindingResult result, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Please correct the errors in the form!");
            return "blog-detail";
        }

        if (authentication == null || authentication.getName() == null) {
            model.addAttribute("error", "User authentication required.");
            return "redirect:/login-user";
        }

        String username = authentication.getName();
        Optional<User> optionalUser = getUserRepository().findByEmail(username);
        if (optionalUser.isPresent()) {
            try {
                User user = optionalUser.get();
                comment.setUser(user);
                Blog blog = blogRepository.findById(comment.getBlog().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));
                comment.setBlog(blog);
                comment.setDatePosted(new Date());
                commentRepository.save(comment);

                redirectAttributes.addFlashAttribute("commentAdded", true);
                return "redirect:/blog/" + comment.getBlog().getId();
            } catch (Exception e) {
                // Log the exception with stack trace
                e.printStackTrace();
                model.addAttribute("error", "An error occurred while adding the comment.");
                return "blog-detail";
            }
        } else {
            model.addAttribute("error", "User not found.");
            return "redirect:/login-user";
        }
    }

    @PostMapping("/search")
    public String search(@RequestParam("searchString") String keyword, Model model) {
        List<Blog> blogs = blogRepository.searchByTitle(keyword);
        model.addAttribute("blogs", blogs);
        model.addAttribute("searchedFor", keyword);
        return "search-results";
    }

    @GetMapping("/getAllBlogs")
    public DeferredResult<String> getAllBlogs(Model model) {
        DeferredResult<String> deferredResult = new DeferredResult<>();
        asyncExecutor.execute(() -> {
            model.addAttribute("blogs", getBlogs());
            deferredResult.setResult("blog-list");
        });
        return deferredResult;
    }

    private Iterable<Blog> getBlogs() {
        logger.info("Getting all blogs, using spring executor thread");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException exception) {
            throw new RuntimeException();
        }
        return blogRepository.findAll();
    }
}
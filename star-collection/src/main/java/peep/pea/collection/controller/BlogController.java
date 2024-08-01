package peep.pea.collection.controller;

import peep.pea.collection.beans.Blog;
import peep.pea.collection.beans.Comment;
import peep.pea.collection.beans.User;
import peep.pea.collection.dao.BlogRepository;
import peep.pea.collection.dao.CommentRepository;
import peep.pea.collection.dao.UserRepository;
import peep.pea.collection.dto.UserCommentDto;
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
                UserCommentDto commentDto = new UserCommentDto();
                commentDto.setBlogId(blog.getId());
                model.addAttribute("commentDto", commentDto);
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
    public String addComment(@ModelAttribute("commentDto") @Valid UserCommentDto commentDto, BindingResult result,
                             Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Please correct the errors in the form.");
            return getBlogDetails(commentDto.getBlogId(), model);
        }

        if (commentDto.getBlogId() == null) {
            System.out.println("Blog ID is null");
            model.addAttribute("error", "Blog ID is missing.");
            return "redirect:/blogs";
        }

        Optional<User> userOpt = getUserRepository().findByEmail(authentication.getName());
        Optional<Blog> blogOpt = blogRepository.findById(commentDto.getBlogId());

        if (userOpt.isPresent() && blogOpt.isPresent()) {
            User user = userOpt.get();
            Blog blog = blogOpt.get();

            Comment comment = new Comment();
            comment.setBlog(blog);
            comment.setUser(user);
            comment.setComment(commentDto.getComment());
            comment.setDatePosted(new Date());

            commentRepository.save(comment);

            redirectAttributes.addFlashAttribute("message", "Comment added successfully.");
            return "redirect:/blog/" + commentDto.getBlogId();
        } else {
            model.addAttribute("error", "Error adding comment. Please try again.");
            return getBlogDetails(commentDto.getBlogId(), model);
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
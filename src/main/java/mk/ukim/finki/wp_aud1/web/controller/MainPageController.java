package mk.ukim.finki.wp_aud1.web.controller;

import mk.ukim.finki.wp_aud1.model.Account;
import mk.ukim.finki.wp_aud1.model.Post;
import mk.ukim.finki.wp_aud1.model.Role;
import mk.ukim.finki.wp_aud1.repository.jpa.AccountJpa;
import mk.ukim.finki.wp_aud1.repository.jpa.PostJpa;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@SessionAttributes("account")
public class MainPageController {

    private final PostJpa postRepository;

    private final AccountJpa accountRepository;

    public MainPageController(PostJpa postRepository, AccountJpa accountRepository) {
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/home")
    public String getHomePage(Model model)
    {
        Account account = (Account) model.getAttribute("account");
        Account updated_account = accountRepository.findById(account.getId()).orElse(null);
        model.addAttribute("account",account);
        List<Post> posts = new ArrayList<>();
        posts = postRepository.findAll();
        Collections.reverse(posts);
        model.addAttribute("posts", posts);
        model.addAttribute("user", updated_account);
        System.out.println(account.getRole());
        if(updated_account.getRole().equals(Role.USER))
        {
            return "MainPage";
        }
        model.addAttribute("allUsers", accountRepository.findAll());
        return "MainPageAdmin";
    }
    @PostMapping("/post")
    public String createPost(@RequestParam String postContent, Model model)
    {
        Post post = new Post();
        post.setPostMessage(postContent);
        if(post.getPostMessage()!=null) {
            post.setAccount((Account) model.getAttribute("account"));
            postRepository.save(post);
        }
        return "redirect:/home";
    }
    @PostMapping("/deletePost/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postRepository.delete(postRepository.findById(postId).orElse(null));
        return "redirect:/home";
    }
    @GetMapping("/viewUser/{userId}")
    public String viewUser(@PathVariable Long userId, Model model){
        Account account = (Account) model.getAttribute("account");
        Account updated_account = accountRepository.findById(account.getId()).orElse(null);
        Account acc = accountRepository.findById(userId).orElse(null);
        model.addAttribute("user",acc);
        if(updated_account.getRole().equals(Role.ADMIN))
        {
            return "viewUser";
        }
        if(updated_account.getRole().equals(Role.SUPER_ADMIN))
        {
            return "viewUserSuperAdmin";
        }
        return null;
    }
}
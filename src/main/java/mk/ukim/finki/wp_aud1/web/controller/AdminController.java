package mk.ukim.finki.wp_aud1.web.controller;

import jakarta.servlet.http.HttpSession;
import mk.ukim.finki.wp_aud1.model.Account;
import mk.ukim.finki.wp_aud1.model.Role;
import mk.ukim.finki.wp_aud1.repository.jpa.AccountJpa;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes("account")
@Controller
public class AdminController {

    private final AccountJpa accRepository;


    public AdminController(AccountJpa accRepository) {
        this.accRepository = accRepository;
    }

    @PostMapping("/changePrivileges/{userId}")
    public String changePrivileges(@PathVariable Long userId, @RequestParam Long role, HttpSession httpSession) {
        Account acc = accRepository.findById(userId).orElse(null);

            if(role == 1)
            {
                acc.setRole(Role.USER);
            }
            else acc.setRole(Role.ADMIN);

        accRepository.save(acc);

        return "redirect:/home";
    }
}
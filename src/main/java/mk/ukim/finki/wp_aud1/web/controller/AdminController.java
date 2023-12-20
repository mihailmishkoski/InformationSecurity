package mk.ukim.finki.wp_aud1.web.controller;

import mk.ukim.finki.wp_aud1.model.Account;
import mk.ukim.finki.wp_aud1.model.Role;
import mk.ukim.finki.wp_aud1.repository.jpa.AccountJpa;
import mk.ukim.finki.wp_aud1.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes("account")
@Controller
public class AdminController {

    private final AccountJpa accRepository;

    private final AccountService accountService;


    public AdminController(AccountJpa accRepository, AccountService accountService) {
        this.accRepository = accRepository;
        this.accountService = accountService;
    }

    @PostMapping("/changePrivileges/{userId}")
    public String changePrivileges(@PathVariable Long userId, @RequestParam Long role, Model model) {

        Account this_account =(Account) model.getAttribute("account");

        if(this_account.getRole().equals(Role.ADMIN) || this_account.getRole().equals(Role.SUPER_ADMIN))
        {
            Account acc = accRepository.findById(userId).orElse(null);

            if(role == 0)
            {
                acc.setRole(Role.USER);
            }
            else if(role == 1)
            {
                acc.setRole(Role.ADMIN);
            }
            else
            {
                acc.setRole(Role.SUPER_ADMIN);
            }
            accRepository.save(acc);
        }

        return "redirect:/home";
    }
    @PostMapping("/deleteAccount/{userId}")
    public String deleteAccount(@PathVariable Long userId,
                                @RequestParam(required = false) String reason,
                                Model model) {

        Account thisAccount = (Account) model.getAttribute("account");
        Account accountToDelete = accRepository.findById(userId).orElse(null);

        if (thisAccount.getRoleEnum().ordinal() > accountToDelete.getRoleEnum().ordinal()) {
            accountService.deleteAccount(accountToDelete,reason);
        }
        return "redirect:/home";
    }
}
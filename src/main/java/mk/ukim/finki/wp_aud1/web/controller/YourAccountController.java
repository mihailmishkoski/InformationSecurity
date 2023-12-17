package mk.ukim.finki.wp_aud1.web.controller;


import jakarta.servlet.http.HttpSession;
import mk.ukim.finki.wp_aud1.model.Account;
import mk.ukim.finki.wp_aud1.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("account")
public class YourAccountController {

    private final AccountService as;

    public YourAccountController(AccountService as) {
        this.as = as;
    }

    @GetMapping("/viewAccount")
    public String viewAccount(Model model, HttpSession httpSession)
    {
        Account acc = (Account) model.getAttribute("account");

        model.addAttribute("user",acc);
        return "viewYourProfile";
    }
    @GetMapping("/editAccount")
    public String getEditAccountForm(Model model, HttpSession httpSession)
    {
        Account acc = (Account) httpSession.getAttribute("account");
        model.addAttribute("user",acc);
        return "editYourProfile";
    }
    @PostMapping("/saveAccount")
    public String editAccount(@RequestParam String name,
                              @RequestParam String surname,
                              @RequestParam String country,
                              @RequestParam String email,
                              Model model,
                              HttpSession httpSession) {
        Account acc = (Account) httpSession.getAttribute("account");
        as.editAccount(acc,name,surname,country,email);
        return "redirect:/home";
    }
    @GetMapping("/changePassword")
    public String getChangePasswordForm(Model model)
    {
        Account acc = (Account) model.getAttribute("account");
        model.addAttribute("user",acc);
        return "editPasswordPage";
    }
    @PostMapping("/changePassword")
    public String ChangePassword(Model model, @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String repeatPassword)
    {
        Account acc = (Account) model.getAttribute("account");
        StringBuilder sb = as.changePassword(acc,currentPassword,newPassword,repeatPassword);
        if(!sb.isEmpty())
        {
            model.addAttribute("error", sb.toString());
            return "editPasswordPage";
        }
        return "redirect:/home";
    }
}
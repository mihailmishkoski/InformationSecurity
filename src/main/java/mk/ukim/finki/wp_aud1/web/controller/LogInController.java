package mk.ukim.finki.wp_aud1.web.controller;

import mk.ukim.finki.wp_aud1.model.Account;
import mk.ukim.finki.wp_aud1.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LogInController {

    private final AccountService as;
    public LogInController(AccountService as)
    {
        this.as = as;
    }




    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String logInPage(@RequestParam(required = false) String username,
                            @RequestParam(required = false) String email,
                            @RequestParam(required = false) String password,
                            Model model) {
        if(username!=null && email!=null && password!=null) {
            if(as.Authenticate(username,password,email))
            {
                Account account = as.findByEmail(email);
                as.generateOtp(account);
                model.addAttribute("account",account);
                return "twoFactorPage";
            }
            model.addAttribute("errorMessage","No user found with those credentials");
        }

        return "logInPage";
    }
    @PostMapping("/twoFactor")
    public String twoFactorAuthentication(@RequestParam Integer code,
                                          @RequestParam String email,
                                          Model model)
    {
        if(code == as.findByEmail(email).getTwoFactorcode())
        {
            return "MainPage";
        }
        model.addAttribute("errorMessage","No user found with those credentials");
        return "logInPage";
    }

    @GetMapping("/create")
    public String getCreateAccountPage() {
        return "createAccount";
    }

    @PostMapping("/create/save")
    public String saveAccount(@RequestParam String name,
                              @RequestParam String surname,
                              @RequestParam String country,
                              @RequestParam String username,
                              @RequestParam String email,
                              @RequestParam String password,
                              @RequestParam String repeatPassword,
                              Model model)
    {

        boolean allGood = true;
        if(!as.checkPasswords(password,repeatPassword))
        {
            allGood = false;
            model.addAttribute("error","Passwords do not match");

        }
        if(!as.isValidPassword(password))
        {
            allGood = false;
            model.addAttribute("passwordError","Password should contain at least 1 special character, uppercase and to be longer than 8 characters");
        }
        if(!as.isValidEmail(email))
        {
            allGood = false;
            model.addAttribute("emailError","Email is not valid");
        }

        model.addAttribute("name", name);
        model.addAttribute("username",username);
        model.addAttribute("surname", surname);
        model.addAttribute("email",email);
        model.addAttribute("password", password);
        model.addAttribute("repeatPassword",repeatPassword);
        model.addAttribute("country",country);
        if(allGood)
        {
            if(as.create(name,surname,username,country,email,password))
            {
                return "redirect:/login";
            }
            model.addAttribute("error","Username already exists");
        }
            return "createAccount";

    }
}

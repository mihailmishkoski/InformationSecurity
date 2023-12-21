package mk.ukim.finki.wp_aud1.web.controller;

import jakarta.servlet.http.HttpSession;
import mk.ukim.finki.wp_aud1.model.Account;
import mk.ukim.finki.wp_aud1.model.Role;
import mk.ukim.finki.wp_aud1.service.AccountService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
@SessionAttributes("account")
public class LogInController {

    private final AccountService as;
    private final JavaMailSender javaMailSender;

    public LogInController(AccountService as, JavaMailSender javaMailSender) {
        this.as = as;
        this.javaMailSender = javaMailSender;
    }


    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String logInPage(@RequestParam(required = false) String username,
                            @RequestParam(required = false) String email,
                            @RequestParam(required = false) String password,
                            Model model) {
        if (username != null && email != null && password != null) {
            if (as.Authenticate(username, password, email)) {
                Account account = as.findByEmail(email);
                as.generateOtp(account);

                model.addAttribute("account", account);
                return "twoFactorPage";
            }
            model.addAttribute("errorMessage", "No user found with those credentials");
        }

        return "logInPage";
    }

    @PostMapping("/twoFactor")
    public String twoFactorAuthentication(@RequestParam Integer code,
                                          @RequestParam String email,
                                          HttpSession session,
                                          Model model) {
        Account acc = as.findByEmail(email);
        if (code == acc.getTwoFactorcode()) {
            session.setAttribute("account", acc);
            return "redirect:/home";
        }
        model.addAttribute("errorMessage", "No user found with those credentials");
        return "logInPage";
    }

    @GetMapping("/create")
    public String getCreateAccountPage() {
        return "createAccount";
    }

    @PostMapping("/create/save")
    public String CheckCredentials(@RequestParam String name,
                                   @RequestParam String surname,
                                   @RequestParam String country,
                                   @RequestParam String username,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam String repeatPassword,
                                   Model model, HttpSession session) {

        boolean allGood = true;
        if (!as.checkPasswords(password, repeatPassword)) {
            allGood = false;
            model.addAttribute("error", "Passwords do not match");

        }
        if (!as.isValidPassword(password)) {
            allGood = false;
            model.addAttribute("passwordError", "Password should contain at least 1 special character, uppercase and to be longer than 8 characters");
        }
        if (!as.isValidEmail(email)) {
            allGood = false;
            model.addAttribute("emailError", "Email is not valid");
        }
        if (!as.isEmailUnique(email)) {
            allGood = false;
            model.addAttribute("emailError", "Email is not valid");
        }

        model.addAttribute("name", name);
        model.addAttribute("username", username);
        model.addAttribute("surname", surname);
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        model.addAttribute("repeatPassword", repeatPassword);
        model.addAttribute("country", country);
        if (allGood) {
            int code = as.mailVerification(email);
            session.setAttribute("userDetails", new Account(name, surname, username, country, email, password, Role.USER));
            session.setAttribute("verificationCode", code);
            return "emailVerification";
        }
        return "createAccount";
    }

    @PostMapping("/create/save/emailValidation")
    public String EmailVerification(@RequestParam Integer code,
                                    Model model, HttpSession session) {
        Account userDetails = (Account) session.getAttribute("userDetails");
        Integer storedCode = (Integer) session.getAttribute("verificationCode");

        if (userDetails != null && storedCode != null && storedCode.equals(code)) {
            // Save the account in the database
            as.create(userDetails.getName(), userDetails.getSurname(), userDetails.getUsername(),
                    userDetails.getCountry(), userDetails.getEmail(), userDetails.getPassword());

            // Clear the session attributes
            session.removeAttribute("userDetails");
            session.removeAttribute("verificationCode");

            return "redirect:/login";
        }

        model.addAttribute("emailError", "Email verification failed.");
        return "createAccount";
    }
}
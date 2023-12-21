package mk.ukim.finki.wp_aud1.service.impl;

import mk.ukim.finki.wp_aud1.bootstrap.DataHolder;
import mk.ukim.finki.wp_aud1.model.Account;
import mk.ukim.finki.wp_aud1.model.Role;
import mk.ukim.finki.wp_aud1.repository.jpa.AccountJpa;
import mk.ukim.finki.wp_aud1.security.PasswordHashing;
import mk.ukim.finki.wp_aud1.service.AccountService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountServiceImpl implements AccountService {


    private final AccountJpa accountJpa;

    private final PasswordHashing ph;


    private final JavaMailSender javaMailSender;
    public AccountServiceImpl(AccountJpa accountJpa, PasswordHashing ph,JavaMailSender javaMailSender)
    {
        this.accountJpa = accountJpa;
        this.ph = ph;
        this.javaMailSender = javaMailSender;
    }
    @Transactional
    @Override
    public Account create(String name, String surname, String username, String country, String email, String password) {
//        String key = emailA;
//        if (!DataHolder.credentialsMap.containsKey(key)) {
            String encodedPassword = ph.passwordEncoder().encode(password);
            Account newAccount = new Account(name, surname, username, country, email, encodedPassword, Role.USER);
            DataHolder.credentialsMap.put(email, newAccount);
            return accountJpa.save(newAccount);//here is saving in database
//            return true;

    }
    @Override
    public boolean isEmailUnique(String email)
    {
        return !DataHolder.credentialsMap.containsKey(email);
    }

    @Override
    public boolean Authenticate(String username, String password, String email) {
        String key = email;
        Account account = DataHolder.credentialsMap.get(key);
        return account != null && ph.passwordEncoder().matches(password,account.getPassword()) && account.getEmail().matches(email);
    }
    @Override
    public boolean checkPasswords(String password, String repeatedPassword)
    {
        return password.matches(repeatedPassword);
    }
    @Override
    public boolean isValidPassword(String password)
    {
        boolean hasMinimumLength = password.length() >= 8;
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasSpecialCharacter = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        return hasMinimumLength && hasUppercase && hasSpecialCharacter;
    }
    @Override
    public boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void generateOtp(Account account) {
            int randomPIN = (int) (Math.random() * 9000) + 1000;
            account.setTwoFactorcode(randomPIN);
            accountJpa.save(account);
            SimpleMailMessage msg = new SimpleMailMessage();
            String email = account.getEmail();
            msg.setTo(email);
            msg.setFrom("dmgameplays829@gmail");

            msg.setSubject("Welcome");
            msg.setText("Hello \n\n" +"Your Login OTP: " + randomPIN + " Please Verify.");

            javaMailSender.send(msg);
    }
    @Override
    public Account findByEmail(String email)
    {
        return DataHolder.credentialsMap.get(email);
    }

    @Override
    public void editAccount(Account account, String name, String surname, String country, String email)
    {
//        StringBuilder sb = new StringBuilder();
//        if(!ph.passwordEncoder().matches(currentPassword,account.getPassword()))
//        {
//            sb.append("Password does not match!");
//            sb.append("\n");
//        }
//        if(!newPassword.matches(repeatedPassword))
//        {
//            sb.append("New or repeated password do not match!");
//            sb.append("\n");
//        }
//        if(!isValidPassword(newPassword))
//        {
//            sb.append("Password is not strong, use at least one special sign, one upper letter, one number and it needs to be longer than 8 characters!");
//            sb.append("\n");
//        }
//        if(!isValidEmail(email))
//        {
//            sb.append("Email is not in valid format!");
//            sb.append("\n");
//        }
//        if(sb.isEmpty())
//        {
            account.setName(name);
            account.setSurname(surname);
            account.setEmail(email);
            account.setCountry(country);
            accountJpa.save(account);
//        }
//        return sb;
    }
    @Override
    public StringBuilder changePassword(Account account, String currentPassword, String newPassword, String repeatedPassword)
    {
        StringBuilder sb = new StringBuilder();
        if(!ph.passwordEncoder().matches(currentPassword,account.getPassword()))
        {
            sb.append("Current password is not correct!");
            sb.append("\n");
        }
        if(!newPassword.matches(repeatedPassword))
        {
            sb.append("New or repeated password do not match!");
            sb.append("\n");
        }
        if(!isValidPassword(newPassword))
        {
            sb.append("Password is not strong, use at least one special sign, one upper letter, one number and it needs to be longer than 8 characters!");
            sb.append("\n");
        }
        if(sb.isEmpty())
        {
            account.setPassword(ph.passwordEncoder().encode(newPassword));
            accountJpa.save(account);
        }
        return sb;
    }
    @Override
    public void deleteAccount(Account acc, String reason){
        SimpleMailMessage msg = new SimpleMailMessage();
        String email = acc.getEmail();
        msg.setTo(email);
        msg.setFrom("dmgameplays829@gmail");

        if (reason != null && !reason.isEmpty()) {

            msg.setSubject("Account Deletion");
            msg.setText("Hello,\n\nYour account has been deleted. Reason: " + reason);
        } else {

            msg.setSubject("Account Deletion");
            msg.setText("Hello,\n\nYour account has been deleted.");
        }
        javaMailSender.send(msg);
        accountJpa.delete(acc);
    }
    public int mailVerification(String user_email)
    {
        int randomPIN = (int) (Math.random() * 9000) + 1000;
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user_email);
        msg.setFrom("dmgameplays829@gmail");

        msg.setSubject("Welcome");
        msg.setText("Hello \n\n" +"Your Email Verification Code: " + randomPIN + " Please Verify.");
        javaMailSender.send(msg);
        return randomPIN;
    }
    @Override
    public void delete(Long id)
    {
        accountJpa.delete(accountJpa.findById(id).orElse(null));
    }

    @Override
    public Account findById(Long id)
    {
        return accountJpa.findById(id).orElse(null);
    }

}

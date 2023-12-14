package mk.ukim.finki.wp_aud1.service.impl;

import mk.ukim.finki.wp_aud1.bootstrap.DataHolder;
import mk.ukim.finki.wp_aud1.model.Account;
import mk.ukim.finki.wp_aud1.repository.InMemoryAccountRepository;
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
    private final InMemoryAccountRepository imar;

    private final AccountJpa accountJpa;

    private final PasswordHashing ph;


    private final JavaMailSender javaMailSender;
    public AccountServiceImpl(InMemoryAccountRepository imar, AccountJpa accountJpa, PasswordHashing ph,JavaMailSender javaMailSender)
    {
        this.imar = imar;
        this.accountJpa = accountJpa;
        this.ph = ph;
        this.javaMailSender = javaMailSender;
    }
    @Transactional
    @Override
    public boolean create(String name, String surname, String username, String country, String email, String password) {
        String key = email;
        if (!DataHolder.credentialsMap.containsKey(key)) {
            String encodedPassword = ph.passwordEncoder().encode(password);
            Account newAccount = new Account(name, surname, username, country, email, encodedPassword);
            DataHolder.credentialsMap.put(key, newAccount);
            accountJpa.save(newAccount);//here is saving in database
            return true;
        }
        return false;
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
        msg.setFrom("dmgameplays829@gmail.com");

        msg.setSubject("Welcome");
            msg.setText("Hello \n\n" +"Your Login OTP: " + randomPIN + " Please Verify.");

            javaMailSender.send(msg);

    }
    @Override
    public Account findByEmail(String email)
    {
        return DataHolder.credentialsMap.get(email);
    }


}

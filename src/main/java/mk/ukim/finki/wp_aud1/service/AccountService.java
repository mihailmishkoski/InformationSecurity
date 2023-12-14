package mk.ukim.finki.wp_aud1.service;

import mk.ukim.finki.wp_aud1.model.Account;

public interface AccountService {
    boolean create(String name, String surname, String username, String country, String email, String password);
    boolean Authenticate(String username, String email, String password);
    boolean checkPasswords(String password, String repeatedPassword);
    boolean isValidPassword(String password);
    boolean isValidEmail(String email);

    void generateOtp(Account account);

    Account findByEmail(String email);
}
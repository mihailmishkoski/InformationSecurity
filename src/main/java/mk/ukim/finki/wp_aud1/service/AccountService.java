package mk.ukim.finki.wp_aud1.service;

import mk.ukim.finki.wp_aud1.model.Account;

public interface AccountService {
    Account create(String name, String surname, String username, String country, String email, String password);
    boolean Authenticate(String username, String email, String password);
    boolean checkPasswords(String password, String repeatedPassword);
    boolean isValidPassword(String password);
    boolean isEmailUnique(String email);
    boolean isValidEmail(String email);

    void generateOtp(Account account);

    Account findByEmail(String email);

    void editAccount(Account account, String name, String surname, String country, String email);
    StringBuilder changePassword(Account acc, String currentPassword, String newPassword, String repeatedPassword);

    void deleteAccount(Account acc, String reason);
    int mailVerification(String user_email);
    void delete(Long id);

    Account findById(Long id);
}

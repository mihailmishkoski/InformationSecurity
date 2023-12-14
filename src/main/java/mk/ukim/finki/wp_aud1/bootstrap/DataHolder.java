package mk.ukim.finki.wp_aud1.bootstrap;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wp_aud1.model.Account;
import mk.ukim.finki.wp_aud1.repository.jpa.AccountJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataHolder {

    public static Map<String, Account> credentialsMap = new HashMap<>();

    @Autowired
    private AccountJpa accountJpa;



    @PostConstruct
    public void init()
    {
        List<Account> accountsFromDatabase = accountJpa.findAll();
        for (Account account : accountsFromDatabase) {
            credentialsMap.put(account.getEmail(), account);
        }
        accountsFromDatabase.clear();
    }

}

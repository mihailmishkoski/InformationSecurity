package mk.ukim.finki.wp_aud1.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Account {
    @Id
    @Column(name = "username_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String surname;


    private String username;

    private String country;

    @Column(unique = true)
    private String email;

    private String password;


    private int twoFactorcode;

    public Account(String name, String surname, String username, String country, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.country = country;
        this.email = email;
        this.password = password;
    }




//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Account account = (Account) o;
//        return Objects.equals(username, account.username) && Objects.equals(email, account.email);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(username, email);
//    }
}

package mk.ukim.finki.wp_aud1.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Account implements Serializable {
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

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Post> posts;

    private Role role;
    public Account(String name, String surname, String username, String country, String email, String password, Role role) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.country = country;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", twoFactorcode=" + twoFactorcode +
                ", posts=" + posts +
                ", role=" + role +
                '}';
    }
    public Role getRoleEnum() {
        return Role.values()[this.getRole().ordinal()];
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

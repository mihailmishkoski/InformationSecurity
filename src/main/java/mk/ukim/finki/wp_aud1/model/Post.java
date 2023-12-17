package mk.ukim.finki.wp_aud1.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "posts")
public class Post {

    private String postMessage;
    @ManyToOne
    private Account account;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Post(String postMessage, Account account) {
        this.account = account;
        this.postMessage = postMessage;
    }

    public Post() {
    }
}

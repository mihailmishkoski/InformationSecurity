package mk.ukim.finki.wp_aud1.repository.jpa;

import mk.ukim.finki.wp_aud1.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostJpa extends JpaRepository<Post, Long> {
}

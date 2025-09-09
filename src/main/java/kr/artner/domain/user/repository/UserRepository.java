package kr.artner.domain.user.repository;

import kr.artner.domain.user.OAuthProvider;
import kr.artner.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByOauthProviderAndEmail(OAuthProvider oauthProvider, String email);

    Boolean existsByEmail(String email);
    
    Boolean existsByUsername(String username);
}

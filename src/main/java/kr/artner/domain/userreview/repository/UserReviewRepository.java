package kr.artner.domain.userreview.repository;

import kr.artner.domain.user.entity.User;
import kr.artner.domain.userreview.entity.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
    Optional<UserReview> findByUserAndTargetUser(User user, User targetUser);
}

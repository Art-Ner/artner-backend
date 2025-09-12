package kr.artner.domain.userreview.repository;

import kr.artner.domain.userreview.entity.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
}

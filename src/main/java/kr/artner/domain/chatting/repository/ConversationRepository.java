package kr.artner.domain.chatting.repository;

import kr.artner.domain.chatting.entity.Conversation;
import kr.artner.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByUserLowAndUserHigh(User userLow, User userHigh);
    Page<Conversation> findByUserLowOrUserHighOrderByCreatedAtDesc(User userLow, User userHigh, Pageable pageable);
}

package kr.artner.domain.chatting.repository;

import kr.artner.domain.chatting.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}

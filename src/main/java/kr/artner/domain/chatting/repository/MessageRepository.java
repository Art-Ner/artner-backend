package kr.artner.domain.chatting.repository;

import kr.artner.domain.chatting.entity.Conversation;
import kr.artner.domain.chatting.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findTopByConversationOrderByCreatedAtDesc(Conversation conversation);
    Page<Message> findByConversationOrderByCreatedAtDesc(Conversation conversation, Pageable pageable);
}

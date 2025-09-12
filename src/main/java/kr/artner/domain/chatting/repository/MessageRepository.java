package kr.artner.domain.chatting.repository;

import kr.artner.domain.chatting.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}

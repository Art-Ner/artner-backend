package kr.artner.global.config;

import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.repository.UserRepository;
import kr.artner.global.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            log.info("WebSocket CONNECT attempt with token: {}", token != null ? "present" : "missing");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                try {
                    Long userId = jwtTokenProvider.getUserIdFromToken(token);
                    Optional<User> userOpt = userRepository.findById(userId);

                    if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                            user, null, null);
                        accessor.setUser(authentication);
                        log.info("WebSocket authenticated user: {}", user.getId());
                    } else {
                        log.warn("User not found for ID: {}", userId);
                    }
                } catch (Exception e) {
                    log.error("WebSocket authentication failed: {}", e.getMessage());
                    // Don't fail the connection, just log the error
                }
            } else {
                log.warn("No valid Authorization header found");
            }
        }

        return message;
    }
}
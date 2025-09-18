package kr.artner.domain.chatting.service;

import kr.artner.domain.chatting.dto.ChattingRequest;
import kr.artner.domain.chatting.dto.ChattingResponse;
import kr.artner.domain.chatting.entity.Conversation;
import kr.artner.domain.chatting.entity.Message;
import kr.artner.domain.chatting.repository.ConversationRepository;
import kr.artner.domain.chatting.repository.MessageRepository;
import kr.artner.domain.user.dto.UserConverter;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.repository.UserRepository;
import kr.artner.global.exception.ErrorStatus;
import kr.artner.global.exception.GeneralException;
import kr.artner.response.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChattingService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public ChattingResponse.CreateConversationResponse createConversation(User currentUser, ChattingRequest.CreateConversation request) {
        User targetUser = userRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if (currentUser.getId().equals(targetUser.getId())) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST);
        }

        User userLow = currentUser.getId() < targetUser.getId() ? currentUser : targetUser;
        User userHigh = currentUser.getId() < targetUser.getId() ? targetUser : currentUser;

        Optional<Conversation> existingConversation = conversationRepository.findByUserLowAndUserHigh(userLow, userHigh);
        if (existingConversation.isPresent()) {
            Conversation conversation = existingConversation.get();
            User otherUser = conversation.getUserLow().getId().equals(currentUser.getId()) ? conversation.getUserHigh() : conversation.getUserLow();

            return ChattingResponse.CreateConversationResponse.builder()
                    .id(conversation.getId())
                    .otherUser(UserConverter.toGetUserInfoResponse(otherUser))
                    .message("기존 대화방이 있습니다.")
                    .build();
        }

        Conversation conversation = Conversation.builder()
                .userLow(userLow)
                .userHigh(userHigh)
                .createdAt(LocalDateTime.now())
                .build();

        Conversation savedConversation = conversationRepository.save(conversation);
        User otherUser = savedConversation.getUserLow().getId().equals(currentUser.getId()) ? savedConversation.getUserHigh() : savedConversation.getUserLow();

        return ChattingResponse.CreateConversationResponse.builder()
                .id(savedConversation.getId())
                .otherUser(UserConverter.toGetUserInfoResponse(otherUser))
                .message("새로운 대화방이 생성되었습니다.")
                .build();
    }

    public ChattingResponse.GetConversationsResponse getMyConversations(User currentUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Conversation> conversationPage = conversationRepository.findByUserLowOrUserHighOrderByCreatedAtDesc(
                currentUser, currentUser, pageable);

        List<ChattingResponse.ConversationItem> conversationItems = conversationPage.getContent().stream()
                .map(conversation -> {
                    User otherUser = conversation.getUserLow().getId().equals(currentUser.getId())
                            ? conversation.getUserHigh()
                            : conversation.getUserLow();

                    Optional<Message> lastMessage = messageRepository.findTopByConversationOrderByCreatedAtDesc(conversation);
                    ChattingResponse.GetMessageResponse lastMessageResponse = null;

                    if (lastMessage.isPresent()) {
                        Message msg = lastMessage.get();
                        lastMessageResponse = ChattingResponse.GetMessageResponse.builder()
                                .id(msg.getId())
                                .conversationId(msg.getConversation().getId())
                                .sender(UserConverter.toGetUserInfoResponse(msg.getSender()))
                                .body(msg.getBody())
                                .createdAt(msg.getCreatedAt())
                                .build();
                    }

                    return ChattingResponse.ConversationItem.builder()
                            .id(conversation.getId())
                            .otherUser(UserConverter.toGetUserInfoResponse(otherUser))
                            .lastMessage(lastMessageResponse)
                            .createdAt(conversation.getCreatedAt())
                            .build();
                })
                .toList();

        PageInfo pageInfo = PageInfo.builder()
                .totalCount(conversationPage.getTotalElements())
                .limit(size)
                .offset(page * size)
                .hasMore(conversationPage.hasNext())
                .build();

        return ChattingResponse.GetConversationsResponse.builder()
                .conversations(conversationItems)
                .pageInfo(pageInfo)
                .build();
    }

    public ChattingResponse.GetMessagesResponse getMessages(User currentUser, Long conversationId, int page, int size) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CONVERSATION_NOT_FOUND));

        if (!isUserInConversation(currentUser, conversation)) {
            throw new GeneralException(ErrorStatus.ACCESS_DENIED);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messagePage = messageRepository.findByConversationOrderByCreatedAtDesc(conversation, pageable);

        List<ChattingResponse.GetMessageResponse> messageResponses = messagePage.getContent().stream()
                .map(message -> ChattingResponse.GetMessageResponse.builder()
                        .id(message.getId())
                        .conversationId(message.getConversation().getId())
                        .sender(UserConverter.toGetUserInfoResponse(message.getSender()))
                        .body(message.getBody())
                        .createdAt(message.getCreatedAt())
                        .build())
                .toList();

        PageInfo pageInfo = PageInfo.builder()
                .totalCount(messagePage.getTotalElements())
                .limit(size)
                .offset(page * size)
                .hasMore(messagePage.hasNext())
                .build();

        return ChattingResponse.GetMessagesResponse.builder()
                .messages(messageResponses)
                .pageInfo(pageInfo)
                .build();
    }

    @Transactional
    public ChattingResponse.SendMessageResponse sendMessage(User currentUser, Long conversationId, ChattingRequest.SendMessage request) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CONVERSATION_NOT_FOUND));

        if (!isUserInConversation(currentUser, conversation)) {
            throw new GeneralException(ErrorStatus.ACCESS_DENIED);
        }

        Message message = Message.builder()
                .conversation(conversation)
                .sender(currentUser)
                .body(request.getBody())
                .createdAt(LocalDateTime.now())
                .build();

        Message savedMessage = messageRepository.save(message);

        ChattingResponse.SendMessageResponse response = ChattingResponse.SendMessageResponse.builder()
                .id(savedMessage.getId())
                .conversationId(savedMessage.getConversation().getId())
                .sender(UserConverter.toGetUserInfoResponse(savedMessage.getSender()))
                .body(savedMessage.getBody())
                .createdAt(savedMessage.getCreatedAt())
                .message("메시지가 전송되었습니다.")
                .build();

        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, response);

        return response;
    }

    @Transactional
    public void leaveConversation(User currentUser, Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CONVERSATION_NOT_FOUND));

        if (!isUserInConversation(currentUser, conversation)) {
            throw new GeneralException(ErrorStatus.ACCESS_DENIED);
        }

        if (conversation.getUserLow().getId().equals(currentUser.getId())) {
            // userLow가 나가는 경우 - archived 처리만
        } else {
            // userHigh가 나가는 경우 - archived 처리만
        }
    }

    private boolean isUserInConversation(User user, Conversation conversation) {
        return conversation.getUserLow().getId().equals(user.getId()) || conversation.getUserHigh().getId().equals(user.getId());
    }
}

package kr.artner.domain.project.dto;

import kr.artner.domain.project.enums.CollabStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ProjectCollabRequestResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResponse {
        private Long id;
        private Long projectId;
        private Long requesterId;
        private CollabStatus status;
        private String message;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcceptResponse {
        private RequestInfo request;
        private MemberInfo member;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RequestInfo {
            private Long id;
            private Long projectId;
            private Long requesterId;
            private CollabStatus status;
            private LocalDateTime decidedAt;
            private Long decidedBy;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class MemberInfo {
            private Long artistId;
            private LocalDateTime joinedAt;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RejectResponse {
        private RequestInfo request;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RequestInfo {
            private Long id;
            private Long projectId;
            private Long requesterId;
            private CollabStatus status;
            private LocalDateTime decidedAt;
            private Long decidedBy;
        }
    }
}
package kr.artner.domain.project.dto;

import kr.artner.domain.common.enums.GenreCode;
import kr.artner.domain.project.enums.ProjectStatus;
import kr.artner.domain.artist.dto.ArtistResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateProjectResponse {
        private Long id;
        private Long ownerId;
        private String title;
        private String concept;
        private String targetRegion;
        private GenreCode targetGenre;
        private String expectedScale;
        private ProjectStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProjectResponse {
        private Long id;
        private Long ownerId;
        private String title;
        private String concept;
        private String targetRegion;
        private GenreCode targetGenre;
        private String expectedScale;
        private ProjectStatus status;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectSummary {
        private Long id;
        private String title;
        private String targetRegion;
        private GenreCode targetGenre;
        private ProjectStatus status;
        private Integer currentParticipants;
        private LocalDateTime createdAt;
        private OwnerSummary owner;
        private List<ParticipantSummary> participants;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class OwnerSummary {
            private Long id;
            private String username;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ParticipantSummary {
            private Long artistProfileId;
            private String name;
            private String imageUrl;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectListResponse {
        private List<ProjectSummary> items;
        private Integer page;
        private Integer size;
        private Long totalItems;
        private Integer totalPages;
        private Boolean hasNext;
        private List<SortInfo> sort;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SortInfo {
            private String field;
            private String dir;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectDetailResponse {
        private ProjectDetail project;
        private List<MemberSummary> members;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ProjectDetail {
            private Long id;
            private Long ownerId;
            private String title;
            private String concept;
            private String targetRegion;
            private GenreCode targetGenre;
            private String expectedScale;
            private ProjectStatus status;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberSummary {
        private Long artistProfileId;
        private String artistName;
        private String profileImageUrl;
        private LocalDateTime joinedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetProjectResponse {
        private Long id;
        private ArtistResponse.GetArtistProfileResponse owner;
        private String title;
        private String concept;
        private String targetRegion;
        private String targetGenre;
        private String expectedScale;
        private String status;
        private LocalDateTime createdAt;
    }
}

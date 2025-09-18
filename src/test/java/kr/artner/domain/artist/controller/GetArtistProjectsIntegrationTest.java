package kr.artner.domain.artist.controller;

import kr.artner.domain.artist.entity.ArtistProfile;
import kr.artner.domain.artist.repository.ArtistProfileRepository;
import kr.artner.domain.common.enums.GenreCode;
import kr.artner.domain.project.entity.Project;
import kr.artner.domain.project.entity.ProjectMember;
import kr.artner.domain.project.enums.ProjectStatus;
import kr.artner.domain.project.repository.ProjectMemberRepository;
import kr.artner.domain.project.repository.ProjectRepository;
import kr.artner.domain.user.entity.User;
import kr.artner.domain.user.enums.UserRole;
import kr.artner.domain.user.repository.UserRepository;
import kr.artner.global.auth.jwt.JwtTokenProvider;
import kr.artner.global.config.DotenvApplicationInitializer;
import kr.artner.global.config.S3Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@ContextConfiguration(initializers = DotenvApplicationInitializer.class)
class GetArtistProjectsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistProfileRepository artistProfileRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private S3Client s3Client;

    private User artistUser;
    private ArtistProfile artistProfile;
    private String accessToken;

    @BeforeEach
    void setUp() {
        // Create an artist user
        artistUser = userRepository.save(User.builder().email("artist@test.com").username("artist").nickname("artistnickname").role(UserRole.USER).build());
        artistProfile = artistProfileRepository.save(ArtistProfile.builder().user(artistUser).artistName("Test Artist").headline("Headline").bio("Bio").build());
        accessToken = jwtTokenProvider.generateToken(artistUser.getId()).getAccessToken();

        // Create projects owned by the artist
        Project ownedProject1 = projectRepository.save(Project.builder().owner(artistProfile).title("Owned Project 1").concept("Concept 1").targetGenre(GenreCode.POP).status(ProjectStatus.RECRUITING).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build());
        Project ownedProject2 = projectRepository.save(Project.builder().owner(artistProfile).title("Owned Project 2").concept("Concept 2").targetGenre(GenreCode.ETC).status(ProjectStatus.RECRUITED).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build());

        // Create another user and project for collaboration
        User otherUser = userRepository.save(User.builder().email("other@test.com").username("other").nickname("othernickname").role(UserRole.USER).build());
        ArtistProfile otherArtistProfile = artistProfileRepository.save(ArtistProfile.builder().user(otherUser).artistName("Other Artist").headline("Other Headline").bio("Other Bio").build());

        Project collabProject = projectRepository.save(Project.builder().owner(otherArtistProfile).title("Collab Project").concept("Collab Concept").targetGenre(GenreCode.ROCK).status(ProjectStatus.RECRUITING).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build());
        projectMemberRepository.save(ProjectMember.builder().project(collabProject).artist(artistProfile).joinedAt(LocalDateTime.now()).build());
    }

    @Test
    @DisplayName("아티스트가 소유한 프로젝트 목록을 조회한다.")
    void getOwnedProjects() throws Exception {
        mockMvc.perform(get("/api/artists/me/projects")
                .param("isOwner", "true")
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.result.projects.length()").value(2))
                .andExpect(jsonPath("$.result.result.projects[0].title").value("Owned Project 1"))
                .andExpect(jsonPath("$.result.result.projects[1].title").value("Owned Project 2"));
    }

    @Test
    @DisplayName("아티스트가 멤버로 참여한 프로젝트 목록을 조회한다 (소유하지 않은 프로젝트).")
    void getMemberProjects() throws Exception {
        mockMvc.perform(get("/api/artists/me/projects")
                .param("isOwner", "false")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.result.projects.length()").value(1))
                .andExpect(jsonPath("$.result.result.projects[0].title").value("Collab Project"));
    }

    @Test
    @DisplayName("아티스트 프로필이 없는 사용자가 프로젝트 목록을 조회하면 에러가 발생한다.")
    void getProjects_noArtistProfile() throws Exception {
        User nonArtistUser = userRepository.save(User.builder().email("nonartist@test.com").username("nonartist").nickname("nonartistnickname").role(UserRole.USER).build());
        String nonArtistAccessToken = jwtTokenProvider.generateToken(nonArtistUser.getId()).getAccessToken();

        mockMvc.perform(get("/api/artists/me/projects")
                .header("Authorization", "Bearer " + nonArtistAccessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("아티스트 프로필을 찾을 수 없습니다."));
    }
}
package de.neuefische.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository appUserRepository;

    // @WithMockUser
    // @WithMockUser(authorities = "ADMIN")

    @Test
    @DirtiesContext
    @WithMockUser
    void post_returnsBody_whenLoggedIn() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("testBody")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void notLoggedIn_expect401() throws Exception {
        mockMvc.perform(get("/api/admin/users")).andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    @WithMockUser
    void LoggedInNoAdmin_expect403() throws Exception {
        mockMvc.perform(get("/api/admin/users")).andExpect(status().isForbidden());
    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = "ADMIN")
    void LoggedInIsAdmin_expect200() throws Exception {
        mockMvc.perform(get("/api/admin/users")).andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void getMe_returnsExistingDbUser() throws Exception {
        // 1. Existing user erstellen
        appUserRepository.save(new AppUser("123", "username", "avatarUrl", "USER"));

        // 2. Simulierter Github user
        OAuth2User githubUser = new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("USER")),
                Map.of("id", "123", "login", "username", "avatar_url", "avatarUrl"),
                "id"
        );

        // 3. Request prüfen ob gewünschte Antwort zurückkommt.
        mockMvc.perform(get("/api/auth/me")
                        .with(oauth2Login().oauth2User(githubUser)))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                        "id": "123",
                        "username": "username",
                        "avatarUrl": "avatarUrl",
                        "role": "USER"
                        }
                        """));
    }

}

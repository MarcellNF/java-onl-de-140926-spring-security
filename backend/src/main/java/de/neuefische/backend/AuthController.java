package de.neuefische.backend;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/me")
    public Map<String, String> getMe(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return Map.of(
                "username", oAuth2User.getAttributes().get("login").toString(),
                "avatar_url", oAuth2User.getAttributes().get("avatar_url").toString()
        );
    }

}

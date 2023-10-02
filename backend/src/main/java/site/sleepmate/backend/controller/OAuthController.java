package site.sleepmate.backend.controller;


import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.sleepmate.backend.service.OauthService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/oauth")
public class OAuthController {
    /**
     * 카카오 callback
     * [GET] /oauth/kakao/callback
     */
    private final OauthService oauthService;

    @GetMapping("/kakao")
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) throws Exception {
        String token = oauthService.getKakaoAccessToken(code);
        oauthService.createKakaoUser(token);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}

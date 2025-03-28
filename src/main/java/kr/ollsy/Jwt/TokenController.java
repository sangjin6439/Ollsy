package kr.ollsy.Jwt;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ollsy.Jwt.dto.TokenResponse;
import kr.ollsy.global.exception.CustomException;
import kr.ollsy.global.exception.GlobalExceptionCode;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/reissue/access-token")
    public ResponseEntity<TokenResponse> reissueAccessToken(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok(tokenService.reissueAccessToken(authorizationHeader));
    }
}

package kr.ollsy.auth.jwt;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ollsy.auth.jwt.dto.TokenResponse;
import lombok.RequiredArgsConstructor;

@Tag(name = "Token")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/reissue/access-token")
    @Operation(summary = "엑세스 토큰 재발급", description = "엑세스 토큰을 재발급합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "엑세스 토큰 재발급 완료", content = {@Content(mediaType = "application/json")}),
    })
    public ResponseEntity<TokenResponse> reissueAccessToken(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(tokenService.reissueAccessToken(authorizationHeader));
    }
}
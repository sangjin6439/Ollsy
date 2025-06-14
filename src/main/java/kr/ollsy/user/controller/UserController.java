package kr.ollsy.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.ollsy.auth.jwt.dto.CustomOAuth2User;
import kr.ollsy.user.dto.request.UserNicknameUpdateRequest;
import kr.ollsy.user.dto.response.UserResponse;
import kr.ollsy.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Tag(name = "User")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "유저 정보 확인", description = "유저의 정보를 확인합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 확인 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "로그인하지 않은 유저가 시도했습니다", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<UserResponse> findUser(
            @AuthenticationPrincipal CustomOAuth2User user
    ) {
        return ResponseEntity.ok(userService.findUser(user.getName()));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "유저 Nickname 변경", description = "유저 Nickname을 변경합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 Nickname 변경 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "로그인하지 않은 유저가 시도했습니다", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> userNicknameUpdate(
            @PathVariable("id") Long id,
            @RequestBody @Valid UserNicknameUpdateRequest nicknameUpdateRequest
    ) {
        return ResponseEntity.ok(userService.userNicknameUpdate(id, nicknameUpdateRequest));
    }
}
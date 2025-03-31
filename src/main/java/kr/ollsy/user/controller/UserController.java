package kr.ollsy.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.ollsy.user.dto.request.UserNicknameUpdateRequest;
import kr.ollsy.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PatchMapping("/{id}")
    public ResponseEntity<String> userNicknameUpdate(
            @PathVariable("id") Long id,
            @RequestBody UserNicknameUpdateRequest nicknameUpdateRequest
    ){
        return ResponseEntity.ok(userService.userNicknameUpdate(id,nicknameUpdateRequest));
    }
}

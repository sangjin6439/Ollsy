package kr.ollsy.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.RequestingUserName;

import kr.ollsy.global.exception.CustomException;
import kr.ollsy.global.exception.GlobalExceptionCode;
import kr.ollsy.user.domain.User;
import kr.ollsy.user.dto.request.UserNicknameUpdateRequest;
import kr.ollsy.user.dto.response.UserResponse;
import kr.ollsy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public String userNicknameUpdate(Long id, UserNicknameUpdateRequest userNicknameUpdateRequest) {

        validNickname(userNicknameUpdateRequest.getNickname());
        User user = findUserById(id);
        user.updateNickname(userNicknameUpdateRequest.getNickname());

        return "닉네임이 변경되었습니다.";
    }

    private void validNickname(final String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(GlobalExceptionCode.DUPLICATE_NICKNAME);
        }
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException(GlobalExceptionCode.NOT_FOUND));
    }

    public UserResponse findUser(String providerId) {
        User user = userRepository.findByProviderId(providerId);
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }
}

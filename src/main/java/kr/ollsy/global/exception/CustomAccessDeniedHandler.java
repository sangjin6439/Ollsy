package kr.ollsy.global.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 권한 부족 시 403 상태 코드 반환
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
        response.setContentType("application/json;charset=UTF-8");

        // 경고 메시지 반환
        String jsonResponse = "{ \"errorCode\": \"4030\", \"errorMessage\": \"권한이 없는 요청입니다.\" }";
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}

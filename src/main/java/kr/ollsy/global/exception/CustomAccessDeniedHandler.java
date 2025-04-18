package kr.ollsy.global.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("Forbidden Error: 권한이 없는 요청이 들어왔습니다.");

        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
        response.setContentType("application/json;charset=UTF-8");

        String jsonResponse = "{ \"errorCode\": \"4030\", \"errorMessage\": \"권한이 없는 요청입니다.\" }";
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}

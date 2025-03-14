package dding.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/jwt-login")
public class AuthErrorController {

    @GetMapping("/authentication-fail")
    public ResponseEntity<?> authenticationFail() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Authentication Failed", "message", "로그인이 필요합니다."));
    }

    @GetMapping("/authorization-fail")
    public ResponseEntity<?> authorizationFail() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "Authorization Failed", "message", "접근 권한이 없습니다."));
    }
}


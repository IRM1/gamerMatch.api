package com.mew.demo.controller;

import com.mew.demo.dto.AuthLoginRequestDto;
import com.mew.demo.dto.AuthResponseDto;
import com.mew.demo.dto.AuthSignupRequestDto;
import com.mew.demo.dto.UserDto;
import com.mew.demo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

  private final AuthService authService;

  @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthResponseDto> signUp(@Valid @RequestBody AuthSignupRequestDto request) {
    return ResponseEntity.ok(authService.signUp(request));
  }

  @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthLoginRequestDto request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request) {
    authService.logout(request);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> me(HttpServletRequest request) {
    return ResponseEntity.ok(UserDto.fromEntity(authService.requireAuthenticatedUser(request)));
  }
}

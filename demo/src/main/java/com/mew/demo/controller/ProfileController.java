package com.mew.demo.controller;

import com.mew.demo.dto.ProfileUpdateRequestDto;
import com.mew.demo.dto.UserDto;
import com.mew.demo.exception.EntityNotFoundException;
import com.mew.demo.model.User;
import com.mew.demo.service.AuthService;
import com.mew.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {

  private final AuthService authService;
  private final UserService userService;

  @GetMapping
  public ResponseEntity<UserDto> getProfile(HttpServletRequest request) {
    User currentUser = authService.requireAuthenticatedUser(request);
    return ResponseEntity.ok(UserDto.fromEntity(currentUser));
  }

  @PutMapping(path = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> updateProfile(
      HttpServletRequest request, @Valid @RequestBody ProfileUpdateRequestDto profileUpdate)
      throws EntityNotFoundException {
    User currentUser = authService.requireAuthenticatedUser(request);
    return ResponseEntity.ok(userService.updateProfile(currentUser.getUserId(), profileUpdate));
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteProfile(HttpServletRequest request)
      throws EntityNotFoundException {
    User currentUser = authService.requireAuthenticatedUser(request);
    userService.deleteUser(currentUser.getUserId());
    return ResponseEntity.noContent().build();
  }
}

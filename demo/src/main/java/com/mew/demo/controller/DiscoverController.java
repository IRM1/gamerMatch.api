package com.mew.demo.controller;

import com.mew.demo.dto.UserDto;
import com.mew.demo.model.User;
import com.mew.demo.service.AuthService;
import com.mew.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/discover", produces = MediaType.APPLICATION_JSON_VALUE)
public class DiscoverController {

  private final AuthService authService;
  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<UserDto>> discoverUsers(
      HttpServletRequest request, @RequestParam(required = false) String query) {
    User currentUser = authService.requireAuthenticatedUser(request);
    return ResponseEntity.ok(userService.discoverUsers(currentUser.getUserId(), query));
  }
}

package com.mew.demo.controller;

import com.mew.demo.dto.MessageDto;
import com.mew.demo.dto.SendMessageRequestDto;
import com.mew.demo.model.User;
import com.mew.demo.service.AuthService;
import com.mew.demo.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

  private final AuthService authService;
  private final MessageService messageService;

  @GetMapping("/{otherUserId}")
  public ResponseEntity<List<MessageDto>> getConversation(
      HttpServletRequest request, @PathVariable Integer otherUserId) {
    User currentUser = authService.requireAuthenticatedUser(request);
    return ResponseEntity.ok(messageService.getConversation(currentUser, otherUserId));
  }

  @PostMapping(path = "/{otherUserId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MessageDto> sendMessage(
      HttpServletRequest request,
      @PathVariable Integer otherUserId,
      @Valid @RequestBody SendMessageRequestDto messageRequest) {
    User currentUser = authService.requireAuthenticatedUser(request);
    return ResponseEntity.ok(messageService.sendMessage(currentUser, otherUserId, messageRequest));
  }
}

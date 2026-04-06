package com.mew.demo.service;

import com.mew.demo.dto.MessageDto;
import com.mew.demo.dto.SendMessageRequestDto;
import com.mew.demo.exception.ForbiddenOperationException;
import com.mew.demo.model.DirectMessage;
import com.mew.demo.model.User;
import com.mew.demo.repository.DirectMessageRepository;
import com.mew.demo.repository.MatchedUserRepository;
import com.mew.demo.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final DirectMessageRepository directMessageRepository;
  private final UserRepository userRepository;
  private final MatchedUserRepository matchedUserRepository;

  @Transactional(readOnly = true)
  public List<MessageDto> getConversation(User currentUser, Integer otherUserId) {
    ensureMatchedUsers(currentUser.getUserId(), otherUserId);

    return directMessageRepository.findConversation(currentUser.getUserId(), otherUserId).stream()
        .map(message -> MessageDto.fromEntity(message, currentUser.getUserId()))
        .toList();
  }

  @Transactional
  public MessageDto sendMessage(User currentUser, Integer otherUserId, SendMessageRequestDto request) {
    ensureMatchedUsers(currentUser.getUserId(), otherUserId);

    User recipient = userRepository.findById(otherUserId)
        .orElseThrow(() -> new IllegalArgumentException("User with ID " + otherUserId + " not found."));

    DirectMessage message = new DirectMessage();
    message.setSender(currentUser);
    message.setRecipient(recipient);
    message.setMessageBody(request.getMessageBody().trim());

    return MessageDto.fromEntity(directMessageRepository.save(message), currentUser.getUserId());
  }

  private void ensureMatchedUsers(Integer userId, Integer otherUserId) {
    if (matchedUserRepository.countMatchedRelationship(userId, otherUserId) == 0) {
      throw new ForbiddenOperationException(
          "Messaging is only available between users who have matched with each other.");
    }
  }
}

package com.mew.demo.dto;

import com.mew.demo.model.DirectMessage;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MessageDto {
  Integer messageId;
  Integer senderUserId;
  Integer recipientUserId;
  String messageBody;
  LocalDateTime sentAt;
  LocalDateTime readAt;
  boolean sentByCurrentUser;

  public static MessageDto fromEntity(DirectMessage message, Integer currentUserId) {
    return MessageDto.builder()
        .messageId(message.getMessageId())
        .senderUserId(message.getSender().getUserId())
        .recipientUserId(message.getRecipient().getUserId())
        .messageBody(message.getMessageBody())
        .sentAt(message.getSentAt())
        .readAt(message.getReadAt())
        .sentByCurrentUser(message.getSender().getUserId() == currentUserId)
        .build();
  }
}

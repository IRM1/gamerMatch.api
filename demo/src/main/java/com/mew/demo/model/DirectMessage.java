package com.mew.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Direct_Message")
public class DirectMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Message_Id", nullable = false)
  private Integer messageId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "Sender_User_Id", nullable = false)
  private User sender;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "Recipient_User_Id", nullable = false)
  private User recipient;

  @Column(name = "Message_Body", nullable = false, length = 1000)
  private String messageBody;

  @Column(name = "Sent_At", nullable = false)
  private LocalDateTime sentAt;

  @Column(name = "Read_At")
  private LocalDateTime readAt;

  @PrePersist
  public void onCreate() {
    if (sentAt == null) {
      sentAt = LocalDateTime.now();
    }
  }
}

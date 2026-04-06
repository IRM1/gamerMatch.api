package com.mew.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "User_Session")
public class UserSession {

  @Id
  @Column(name = "Session_Token", nullable = false, length = 80)
  private String sessionToken;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "User_Id", nullable = false)
  private User user;

  @Column(name = "Created_At", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "Expires_At", nullable = false)
  private LocalDateTime expiresAt;

  @PrePersist
  public void onCreate() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }
}

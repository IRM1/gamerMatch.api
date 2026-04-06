package com.mew.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "User")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "User_Id", nullable = false)
  private int userId;

  @Column(name = "First_Name", nullable = false, length = 15)
  private String firstName;

  @Column(name = "Last_Name", nullable = true, length = 20)
  private String lastName;

  @Column(name = "Dob", nullable = false)
  private LocalDate dob;

  @Column(name = "Email", nullable = false, length = 50)
  private String email;

  @JsonIgnore
  @Column(name = "Password_Hash", nullable = true, length = 100)
  private String passwordHash;

  @Column(name = "Gamertag", nullable = false, length = 25)
  private String gamertag;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Preferred_Console", referencedColumnName = "Console_Id", nullable = false)
  @ToString.Exclude
  private Console preferredConsole;

  @Column(name = "About_User", nullable = true, length = 500)
  private String aboutUser;

  @ManyToMany
  @JoinTable(
      name = "UserGames",
      joinColumns = @JoinColumn(name = "User_Id"),
      inverseJoinColumns = @JoinColumn(name = "Game_Id"))
  @Builder.Default
  @ToString.Exclude
  @JsonIgnoreProperties({"users"})
  private Set<Game> games = new HashSet<>();

  @Builder.Default
  @JsonIgnore
  @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<MatchedUser> sentLikes = new HashSet<>();

  @Builder.Default
  @JsonIgnore
  @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<MatchedUser> receivedLikes = new HashSet<>();

  @Builder.Default
  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserSession> sessions = new HashSet<>();

  @Builder.Default
  @JsonIgnore
  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<DirectMessage> sentMessages = new HashSet<>();

  @Builder.Default
  @JsonIgnore
  @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<DirectMessage> receivedMessages = new HashSet<>();

  @Column(name = "Created_At")
  private LocalDateTime createdAt;

  @PrePersist
  public void onCreate() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }

  public static User fromOptional(Optional<User> optionalUser, String identifier) {

    return optionalUser.orElseThrow(
        () -> new IllegalArgumentException("User with identifier " + identifier + " not found."));
  }

  public void addSentLike(MatchedUser match) {

    sentLikes.add(match);
    match.setUser1(Optional.of(this));
  }

  public void addReceivedLike(MatchedUser match) {

    receivedLikes.add(match);
    match.setUser2(Optional.of(this));
  }
}

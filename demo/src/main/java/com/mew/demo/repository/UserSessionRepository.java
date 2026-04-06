package com.mew.demo.repository;

import com.mew.demo.model.UserSession;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, String> {

  @Query(
      """
      SELECT session
      FROM UserSession session
      JOIN FETCH session.user
      LEFT JOIN FETCH session.user.games
      LEFT JOIN FETCH session.user.preferredConsole
      WHERE session.sessionToken = :token AND session.expiresAt > :now
      """)
  Optional<UserSession> findActiveSessionByToken(
      @Param("token") String token, @Param("now") LocalDateTime now);

  @Modifying
  @Transactional
  @Query("DELETE FROM UserSession session WHERE session.expiresAt <= :now")
  void deleteExpiredSessions(@Param("now") LocalDateTime now);

  @Modifying
  @Transactional
  @Query("DELETE FROM UserSession session WHERE session.user.userId = :userId")
  void deleteByUserId(@Param("userId") Integer userId);
}

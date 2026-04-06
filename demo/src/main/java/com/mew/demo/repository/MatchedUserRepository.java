package com.mew.demo.repository;

import com.mew.demo.dto.MatchInfoDto;
import com.mew.demo.model.MatchedUser;
import com.mew.demo.model.MatchedUserId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MatchedUserRepository extends JpaRepository<MatchedUser, MatchedUserId> {

  @Query(
      value = "SELECT * FROM Matched_User mu "
          + "WHERE (mu.User_Id = :userId OR mu.Liked_User_Id = :userId) AND mu.Is_Matched = 1",
      nativeQuery = true)
  List<MatchedUser> findAllMatchesForUser(@Param("userId") Integer userId);

  @Query(
      value =
          """
        SELECT u.First_Name AS firstName, u.Gamertag AS gamertag, u.About_User AS aboutUser
        FROM `User` u
        INNER JOIN Matched_User mu
            ON u.User_Id = mu.Liked_User_Id
        WHERE mu.User_Id = :userId
        AND u.User_Id = :matchId
        AND mu.Is_Matched = 1
        """,
      nativeQuery = true)
  MatchInfoDto findMatchInfo(@Param("userId") Integer userId, @Param("matchId") Integer matchId);

  @Query(
      value =
          """
        SELECT *
        FROM Matched_User mu
        WHERE mu.User_Id = :userId AND mu.Liked_User_Id = :matchId
        """,
      nativeQuery = true)
  MatchedUser findMatch(@Param("userId") Integer userId, @Param("matchId") Integer matchId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Transactional
  @Query(
      value =
          """
        UPDATE Matched_User
        SET Is_Matched = :isMatched
        WHERE (User_Id = :userId AND Liked_User_Id = :matchId)
        OR (User_Id = :matchId AND Liked_User_Id = :userId)
        """,
      nativeQuery = true)
  void updateIsMatched(
      @Param("userId") Integer userId,
      @Param("matchId") Integer matchId,
      @Param("isMatched") boolean isMatched);

  @Modifying
  @Transactional
  @Query("DELETE FROM MatchedUser mu WHERE mu.user1.userId = :userId OR mu.user2.userId = :userId")
  void deleteByUserId(@Param("userId") Integer userId);

  @Query(
      value =
          """
          SELECT COUNT(*)
          FROM Matched_User mu
          WHERE (
              (mu.User_Id = :userId AND mu.Liked_User_Id = :otherUserId)
              OR (mu.User_Id = :otherUserId AND mu.Liked_User_Id = :userId)
          )
          AND mu.Is_Matched = 1
          """,
      nativeQuery = true)
  long countMatchedRelationship(
      @Param("userId") Integer userId, @Param("otherUserId") Integer otherUserId);
}

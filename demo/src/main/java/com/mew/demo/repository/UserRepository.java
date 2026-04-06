package com.mew.demo.repository;

import com.mew.demo.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  @Query("SELECT DISTINCT u FROM User u " + "LEFT JOIN FETCH u.games g "
      + "LEFT JOIN FETCH u.preferredConsole")
  List<User> findAllWithGamesAndConsole();

  @Query("SELECT u FROM User u " + "LEFT JOIN FETCH u.games g "
      + "LEFT JOIN FETCH u.preferredConsole "
      + "WHERE u.userId = :userId")
  User findByIdWithGamesAndConsole(Integer userId);

  @Query("SELECT u FROM User u " + "LEFT JOIN FETCH u.games g "
      + "LEFT JOIN FETCH u.preferredConsole "
      + "WHERE u.firstName = :firstName")
  User findByFirstNameWithGamesAndConsole(String firstName);

  Optional<User> findByFirstName(String firstName);

  Optional<User> findByFirstNameIgnoreCase(String firstName);

  Optional<User> findByEmailIgnoreCase(String email);

  boolean existsByEmail(String email);

  boolean existsByGamertag(String gamertag);

  @Query(
      """
      SELECT DISTINCT u
      FROM User u
      LEFT JOIN FETCH u.games g
      LEFT JOIN FETCH u.preferredConsole
      WHERE u.userId <> :currentUserId
        AND u.passwordHash IS NOT NULL
      ORDER BY u.firstName, u.lastName
      """)
  List<User> findDiscoverableUsers(@Param("currentUserId") Integer currentUserId);

  @Query(
      """
      SELECT DISTINCT u
      FROM User u
      LEFT JOIN FETCH u.games g
      LEFT JOIN FETCH u.preferredConsole
      WHERE u.userId <> :currentUserId
        AND u.passwordHash IS NOT NULL
        AND (
          LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
          OR LOWER(COALESCE(u.lastName, '')) LIKE LOWER(CONCAT('%', :query, '%'))
          OR LOWER(u.gamertag) LIKE LOWER(CONCAT('%', :query, '%'))
          OR LOWER(g.gameTitle) LIKE LOWER(CONCAT('%', :query, '%'))
        )
      ORDER BY u.firstName, u.lastName
      """)
  List<User> searchDiscoverableUsers(
      @Param("currentUserId") Integer currentUserId, @Param("query") String query);
}

package com.mew.demo.repository;

import com.mew.demo.model.DirectMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage, Integer> {

  @Query(
      value =
          """
          SELECT *
          FROM Direct_Message dm
          WHERE (
            dm.Sender_User_Id = :userId AND dm.Recipient_User_Id = :otherUserId
          ) OR (
            dm.Sender_User_Id = :otherUserId AND dm.Recipient_User_Id = :userId
          )
          ORDER BY dm.Sent_At ASC
          """,
      nativeQuery = true)
  List<DirectMessage> findConversation(
      @Param("userId") Integer userId, @Param("otherUserId") Integer otherUserId);
}

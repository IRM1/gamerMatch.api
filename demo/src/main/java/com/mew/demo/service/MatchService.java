package com.mew.demo.service;

import com.mew.demo.dto.MatchInfoDto;
import com.mew.demo.dto.MatchedUserDto;
import com.mew.demo.exception.EntityNotFoundException;
import com.mew.demo.model.MatchedUser;
import com.mew.demo.model.MatchedUserId;
import com.mew.demo.model.User;
import java.util.LinkedHashMap;
import com.mew.demo.repository.MatchedUserRepository;
import com.mew.demo.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchService {

  @Autowired
  private final MatchedUserRepository matchedUserRepository;

  @Autowired
  private final UserRepository userRepository;

  public List<MatchedUserDto> getAllMatchesForUser(Integer userId) throws EntityNotFoundException {

    List<MatchedUser> matches = matchedUserRepository.findAllMatchesForUser(userId);

    if (matches.isEmpty()) {
      return List.of();
    }

    LinkedHashMap<Integer, MatchedUserDto> uniqueMatches = new LinkedHashMap<>();

    for (MatchedUser match : matches) {
      int counterpartUserId =
          match.getUser1().getUserId() == userId
              ? match.getUser2().getUserId()
              : match.getUser1().getUserId();
      uniqueMatches.putIfAbsent(counterpartUserId, MatchedUserDto.fromEntity(match));
    }

    return uniqueMatches.values().stream().collect(Collectors.toList());
  }

  public MatchInfoDto getMatchInfo(Integer userId, Integer matchId) {

    return matchedUserRepository.findMatchInfo(userId, matchId);
  }

  @Transactional(readOnly = true)
  public List<MatchedUserDto> getMatchesForCurrentUser(User currentUser)
      throws EntityNotFoundException {
    return getAllMatchesForUser(currentUser.getUserId());
  }

  @Transactional
  public void updateMatchStatus(Integer userId, Integer matchId) {
    MatchedUserId currentId = new MatchedUserId(userId, matchId);
    MatchedUserId reciprocalId = new MatchedUserId(matchId, userId);

    MatchedUser existing = matchedUserRepository.findById(currentId).orElse(null);
    MatchedUser reciprocal = matchedUserRepository.findById(reciprocalId).orElse(null);

    if (existing != null && Boolean.TRUE.equals(existing.getIsMatched())) {
      return;
    }

    if (reciprocal != null && Boolean.TRUE.equals(reciprocal.getIsMatched())) {
      if (existing == null) {
        existing = buildMatchRecord(userId, matchId);
      }

      existing.setIsMatched(true);
      matchedUserRepository.save(existing);
      return;
    }

    if (existing == null) {
      existing = buildMatchRecord(userId, matchId);
    }

    if (reciprocal != null) {
      existing.setIsMatched(true);
      reciprocal.setIsMatched(true);
      matchedUserRepository.save(existing);
      matchedUserRepository.save(reciprocal);
      return;
    }

    existing.setIsMatched(false);
    matchedUserRepository.save(existing);
  }

  @Transactional
  public List<MatchedUserDto> likeUser(User currentUser, Integer targetUserId)
      throws EntityNotFoundException {
    if (currentUser.getUserId() == targetUserId) {
      throw new IllegalArgumentException("You cannot like your own profile.");
    }

    updateMatchStatus(currentUser.getUserId(), targetUserId);
    return getAllMatchesForUser(currentUser.getUserId());
  }

  private MatchedUser buildMatchRecord(Integer userId, Integer matchId) {
    MatchedUser newLike = new MatchedUser();
    Optional<User> user1 = userRepository.findById(userId);
    Optional<User> user2 = userRepository.findById(matchId);

    newLike.setId(new MatchedUserId(userId, matchId));
    newLike.setUser1(user1);
    newLike.setUser2(user2);
    return newLike;
  }
}

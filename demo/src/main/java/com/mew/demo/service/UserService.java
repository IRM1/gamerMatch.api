package com.mew.demo.service;

import com.mew.demo.dto.ProfileUpdateRequestDto;
import com.mew.demo.dto.UserDto;
import com.mew.demo.exception.EntityNotFoundException;
import com.mew.demo.model.Game;
import com.mew.demo.model.User;
import com.mew.demo.repository.ConsoleRepository;
import com.mew.demo.repository.GameRepository;
import com.mew.demo.repository.MatchedUserRepository;
import com.mew.demo.repository.UserGamesRepository;
import com.mew.demo.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final ConsoleRepository consoleRepository;
  private final GameRepository gameRepository;
  private final UserGamesRepository userGamesRepository;
  private final MatchedUserRepository matchedUserRepository;

  // private final UserDtoMapper userDtoMapper;

  public List<User> convertDtosToUsers(List<UserDto> userDtos) throws EntityNotFoundException {

    return userDtos.stream()
        .map(dto -> dto.toEntity(consoleRepository, gameRepository))
        .toList();
  }

  @Transactional(readOnly = true)
  public List<User> getAllUsers() throws EntityNotFoundException {

    return userRepository.findAllWithGamesAndConsole().stream().collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public User getUserById(Integer userId) throws EntityNotFoundException {

    Optional<User> optionalUser = userRepository.findById(userId);
    return User.fromOptional(optionalUser, "ID=" + userId);
  }

  @Transactional(readOnly = true)
  public User getUserByFirstName(String firstName) throws EntityNotFoundException {

    Optional<User> optionalUser = userRepository.findByFirstName(firstName);
    return User.fromOptional(optionalUser, "firstName=" + firstName);
  }

  @Transactional
  public UserDto createUser(UserDto userDto) throws EntityNotFoundException {

    if (userRepository.existsByEmail(userDto.getEmail())) {
      throw new EntityExistsException(
          "A user already exists with this email: " + userDto.getEmail());
    }
    if (userRepository.existsByGamertag(userDto.getGamertag())) {
      throw new EntityExistsException(
          "A user already exists with this gamertag: " + userDto.getGamertag());
    }

    User user = userDto.toEntity(consoleRepository, gameRepository);

    return UserDto.fromEntity(userRepository.save(user));
  }

  @Transactional
  public UserDto updateUser(UserDto userDto)
      throws EntityNotFoundException, IllegalArgumentException {

    if (userDto.getUserId() == null) {
      throw new IllegalArgumentException("UserId must be provided for update.");
    }

    User user = getUserById(userDto.getUserId());

    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setDob(userDto.getDob());
    user.setEmail(userDto.getEmail());
    user.setGamertag(userDto.getGamertag());
    user.setPreferredConsole(consoleRepository.getReferenceById(userDto.getConsoleId()));
    user.setAboutUser(userDto.getAboutUser());

    Set<Integer> gameIds = userDto.getGameIds();
    Set<Game> games = gameIds.stream()
        .map(gameId -> gameRepository
            .findById(gameId)
            .orElseThrow(
                () -> new IllegalArgumentException("Game with ID " + gameId + " not found.")))
        .collect(Collectors.toSet());

    user.setGames(games);

    return UserDto.fromEntity(userRepository.save(user));
  }

  @Transactional(readOnly = true)
  public List<UserDto> discoverUsers(Integer currentUserId, String query) {
    List<User> users;

    if (query == null || query.trim().isBlank()) {
      users = userRepository.findDiscoverableUsers(currentUserId);
    } else {
      users = userRepository.searchDiscoverableUsers(currentUserId, query.trim());
    }

    return users.stream().map(UserDto::fromEntity).toList();
  }

  @Transactional
  public UserDto updateProfile(Integer userId, ProfileUpdateRequestDto request)
      throws EntityNotFoundException {
    User user = getUserById(userId);

    if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
      user.setFirstName(request.getFirstName().trim());
    }

    if (request.getLastName() != null) {
      user.setLastName(request.getLastName().trim());
    }

    if (request.getDob() != null) {
      user.setDob(request.getDob());
    }

    if (request.getGamertag() != null && !request.getGamertag().isBlank()) {
      user.setGamertag(request.getGamertag().trim());
    }

    if (request.getConsoleId() != null) {
      user.setPreferredConsole(consoleRepository.getReferenceById(request.getConsoleId()));
    }

    if (request.getAboutUser() != null) {
      user.setAboutUser(request.getAboutUser().trim());
    }

    if (request.getGameIds() != null) {
      Set<Game> games = request.getGameIds().stream()
          .map(gameId -> gameRepository.findById(gameId)
              .orElseThrow(
                  () -> new IllegalArgumentException("Game with ID " + gameId + " not found.")))
          .collect(Collectors.toSet());
      user.setGames(games);
    }

    return UserDto.fromEntity(userRepository.save(user));
  }

  @Transactional
  public void deleteUser(Integer userId) throws EntityNotFoundException {

    if (!userRepository.existsById(userId)) {
      throw new EntityNotFoundException("User with ID " + userId + " not found");
    }

    matchedUserRepository.deleteByUserId(userId);
    userGamesRepository.deleteByUserId(userId);
    userRepository.deleteById(userId);
  }
}

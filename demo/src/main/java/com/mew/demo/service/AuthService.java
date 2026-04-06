package com.mew.demo.service;

import com.mew.demo.dto.AuthLoginRequestDto;
import com.mew.demo.dto.AuthResponseDto;
import com.mew.demo.dto.AuthSignupRequestDto;
import com.mew.demo.dto.UserDto;
import com.mew.demo.exception.AuthenticationException;
import com.mew.demo.model.User;
import com.mew.demo.model.UserSession;
import com.mew.demo.repository.ConsoleRepository;
import com.mew.demo.repository.GameRepository;
import com.mew.demo.repository.UserRepository;
import com.mew.demo.repository.UserSessionRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private static final String BEARER_PREFIX = "Bearer ";

  private final UserRepository userRepository;
  private final UserSessionRepository userSessionRepository;
  private final ConsoleRepository consoleRepository;
  private final GameRepository gameRepository;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Value("${app.auth.allowed-email-domains}")
  private String allowedEmailDomains;

  @Transactional
  public AuthResponseDto signUp(AuthSignupRequestDto request) {
    validateAllowedEmailDomain(request.getEmail());

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new EntityExistsException("A user already exists with this email: " + request.getEmail());
    }

    if (userRepository.existsByGamertag(request.getGamertag())) {
      throw new EntityExistsException(
          "A user already exists with this gamertag: " + request.getGamertag());
    }

    User user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .dob(request.getDob())
        .email(normalizeEmail(request.getEmail()))
        .passwordHash(passwordEncoder.encode(request.getPassword()))
        .gamertag(request.getGamertag())
        .preferredConsole(consoleRepository
            .findById(request.getConsoleId())
            .orElseThrow(() ->
                new IllegalArgumentException("Console with ID " + request.getConsoleId() + " not found.")))
        .aboutUser(request.getAboutUser())
        .build();

    if (request.getGameIds() != null && !request.getGameIds().isEmpty()) {
      user.setGames(request.getGameIds().stream()
          .map(gameId -> gameRepository
              .findById(gameId)
              .orElseThrow(() ->
                  new IllegalArgumentException("Game with ID " + gameId + " not found.")))
          .collect(java.util.stream.Collectors.toSet()));
    }

    User savedUser = userRepository.save(user);
    UserSession session = createSession(savedUser);
    return AuthResponseDto.builder()
        .sessionToken(session.getSessionToken())
        .user(UserDto.fromEntity(savedUser))
        .build();
  }

  @Transactional
  public AuthResponseDto login(AuthLoginRequestDto request) {
    User user = userRepository.findByEmailIgnoreCase(normalizeEmail(request.getEmail()))
        .orElseThrow(() -> new AuthenticationException("Invalid email or password."));

    if (user.getPasswordHash() == null
        || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
      throw new AuthenticationException("Invalid email or password.");
    }

    UserSession session = createSession(user);
    return AuthResponseDto.builder()
        .sessionToken(session.getSessionToken())
        .user(UserDto.fromEntity(user))
        .build();
  }

  @Transactional(readOnly = true)
  public User requireAuthenticatedUser(HttpServletRequest request) {
    String token = extractBearerToken(request);
    return userSessionRepository
        .findActiveSessionByToken(token, LocalDateTime.now())
        .map(UserSession::getUser)
        .orElseThrow(() -> new AuthenticationException("You must be signed in to access this resource."));
  }

  @Transactional
  public void logout(HttpServletRequest request) {
    String token = extractBearerToken(request);
    userSessionRepository.deleteById(token);
  }

  private UserSession createSession(User user) {
    userSessionRepository.deleteExpiredSessions(LocalDateTime.now());

    UserSession session = new UserSession();
    session.setSessionToken(UUID.randomUUID().toString());
    session.setUser(user);
    session.setExpiresAt(LocalDateTime.now().plusDays(14));
    return userSessionRepository.save(session);
  }

  private String extractBearerToken(HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
      throw new AuthenticationException("Missing or invalid Authorization header.");
    }

    return authorizationHeader.substring(BEARER_PREFIX.length()).trim();
  }

  private void validateAllowedEmailDomain(String email) {
    String normalizedEmail = normalizeEmail(email);
    List<String> domains = Arrays.stream(allowedEmailDomains.split(","))
        .map(String::trim)
        .map(domain -> domain.toLowerCase(Locale.ROOT))
        .filter(domain -> !domain.isBlank())
        .toList();

    boolean allowed = domains.stream()
        .anyMatch(domain -> normalizedEmail.endsWith("@" + domain));

    if (!allowed) {
      throw new AuthenticationException("Only WSU email addresses can be used to sign up.");
    }
  }

  private String normalizeEmail(String email) {
    return email.trim().toLowerCase(Locale.ROOT);
  }
}
